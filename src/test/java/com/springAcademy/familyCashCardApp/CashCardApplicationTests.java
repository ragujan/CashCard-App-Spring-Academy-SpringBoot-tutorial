package com.springAcademy.familyCashCardApp;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class CashCardApplicationTests {

	@Autowired
	TestRestTemplate restTemplate;

//	Should return a cash card when data is saved
	@Test
	void shouldReturnACashCardWhenDataIsSaved() {
		ResponseEntity<CashCardEntity> response = restTemplate.getForEntity("/cashcards/6", CashCardEntity.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	void fakeTest() {
		ResponseEntity<String> response = restTemplate.getForEntity("/cashcards/fake/99", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
//	Should not return a cash card with an unknown id
	void shouldNotReturnACashCardWithAnUnknownId() {
		ResponseEntity<CashCardEntity> response = restTemplate.getForEntity("/cashcards/100", CashCardEntity.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

//	@Test
////	Should create a new CashCard
//	@DirtiesContext
	void shouldCreateANewCashCard() {
		CashCardEntity cardEntity = new CashCardEntity(333.99);
		ResponseEntity<Void> createResponse = restTemplate.postForEntity("/cashcards", cardEntity, Void.class);
		assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		URI locationOfNewCashCard = createResponse.getHeaders().getLocation();
		ResponseEntity<String> getResponse = restTemplate.getForEntity(locationOfNewCashCard, String.class);
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		System.out.println(getResponse.getBody());
		DocumentContext context = JsonPath.parse(getResponse.getBody());
		Number id = context.read("$.id");
		Double amount = context.read("$.amount");

		assertThat(id).isNotNull();
		assertThat(amount).isEqualTo(333.99);

	}

	@Test
	// Should return all cash card when list is requested
	void shouldReturnAllCashCardWhenListIsRequested() {
		ResponseEntity<String> response = restTemplate.getForEntity("/cashcards", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());

		int cashCardCount = documentContext.read("$.length()");

//		assertThat(cashCardCount).isEqualTo(4);

	}

	@Test
	void shouldReturnAPageOfCashCards() {
		ResponseEntity<String> response = restTemplate.getForEntity("/cashcards?page=0&size=1", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentcontext = JsonPath.parse(response.getBody());
		JSONArray page = documentcontext.read("$[*]");
		assertThat(page.size()).isEqualTo(1);
	}

	@Test
	void shouldReturnASortedPageOfCashCards() {
		ResponseEntity<String> response = restTemplate.getForEntity("/cashcards?page=0&size=1&sort=amount,desc",
				String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		JSONArray read = documentContext.read("$[*]");
		assertThat(read.size()).isEqualTo(1);

		double amount = documentContext.read("$[0].amount");
		assertThat(amount).isEqualTo(777.99);
	}

	@Test
//	Should return a sorted page of cash cards with no parameters and use default values
	void shouldReturnASortedPageOfCashCardsWithNoParametersAndUseDefaultValues() {
		ResponseEntity<String> response = restTemplate.getForEntity("/cashcards", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		JSONArray page = documentContext.read("$[*]");
		assertThat(page.size()).isEqualTo(6);

		JSONArray amounts = documentContext.read("$..amount");
		System.out.println(amounts);
		assertThat(amounts).containsExactly(111.11,222.99,333.99,333.99,444.99,777.99);

	}

}
