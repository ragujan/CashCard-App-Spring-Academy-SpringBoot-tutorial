package com.springAcademy.familyCashCardApp;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// @DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class CashCardApplicationTests {

	@Autowired
	TestRestTemplate restTemplate;

	// Should return a cash card when data is saved
	@Test
	void shouldReturnACashCardWhenDataIsSaved() {
		ResponseEntity<String> response = restTemplate.withBasicAuth("sarah1", "abc123") // Add this
				.getForEntity("/cashcards/6", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	void fakeTest() {
		ResponseEntity<String> response = restTemplate.withBasicAuth("sarah1", "abc123")
				.getForEntity("/cashcards/fake/99", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	// Should not return a cash card with an unknown id
	void shouldNotReturnACashCardWithAnUnknownId() {

		ResponseEntity<String> response = restTemplate.withBasicAuth("sarah1", "abc123") // Add this
				.getForEntity("/cashcards/645", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	// run this test in isolation
	// otherwise comment it out, it adds data to the database, which means you have
	// to change the
	// assert values accordingly in other tests
	// @Test
	// // Should create a new CashCard
	// @DirtiesContext
	void shouldCreateANewCashCard() {
		CashCardEntity cardEntity = new CashCardEntity(333.99, null);
		ResponseEntity<Void> createResponse = restTemplate.withBasicAuth("sarah1", "abc123").postForEntity("/cashcards",
				cardEntity, Void.class);
		assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		URI locationOfNewCashCard = createResponse.getHeaders().getLocation();
		ResponseEntity<String> getResponse = restTemplate.withBasicAuth("sarah1", "abc123")
				.getForEntity(locationOfNewCashCard, String.class);
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
		ResponseEntity<String> response = restTemplate.withBasicAuth("sarah1", "abc123").getForEntity("/cashcards",
				String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());

		int cashCardCount = documentContext.read("$.length()");

		// assertThat(cashCardCount).isEqualTo(4);

	}

	@Test
	void shouldReturnAPageOfCashCards() {
		ResponseEntity<String> response = restTemplate.withBasicAuth("sarah1", "abc123")
				.getForEntity("/cashcards?page=0&size=1", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentcontext = JsonPath.parse(response.getBody());
		JSONArray page = documentcontext.read("$[*]");
		assertThat(page.size()).isEqualTo(1);
	}

	@Test
	void shouldReturnASortedPageOfCashCards() {
		ResponseEntity<String> response = restTemplate.withBasicAuth("sarah1", "abc123")
				.getForEntity("/cashcards?page=0&size=1&sort=amount,desc", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		JSONArray read = documentContext.read("$[*]");
		assertThat(read.size()).isEqualTo(1);

		double amount = documentContext.read("$[0].amount");
		assertThat(amount).isEqualTo(777.99);
	}

	@Test
	// Should return a sorted page of cash cards with no parameters and use default
	// values
	void shouldReturnASortedPageOfCashCardsWithNoParametersAndUseDefaultValues() {
		ResponseEntity<String> response = restTemplate.withBasicAuth("sarah1", "abc123").getForEntity("/cashcards",
				String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		JSONArray page = documentContext.read("$[*]");
		assertThat(page.size()).isEqualTo(6);

		JSONArray amounts = documentContext.read("$..amount");
		System.out.println(amounts);
		assertThat(amounts).containsExactly(111.11, 222.99, 333.99, 333.99, 444.99, 777.99);

	}

	// Should not return a cash card when using bad credentials
	@Test
	void shouldNotReturnACashCardWhenusingBadCredentials() {
		ResponseEntity<String> response = restTemplate.withBasicAuth("bad-user", "111")
				.getForEntity("/cashcards/1", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
		response = restTemplate
				.withBasicAuth("sarah1", "BAD-PASSWORD")
				.getForEntity("/cashcards/99", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	// Should reject users who are not card owners
	@Test
	void shouldRejectUsersWhoAreNotCardOwners() {
		ResponseEntity<String> response = restTemplate.withBasicAuth("hank-owns-no-cards", "qrs456")
				.getForEntity("/cashcards/1", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
	}

	// Should not allow acces to cash cards they do not own
	@Test
	void shouldNotAllowAccessToCashCardsTheyDoNotOwn() {
		ResponseEntity<String> resposne = restTemplate.withBasicAuth("sarah1", "abc123")
				.getForEntity("/cashcards/9", String.class);

		assertThat(resposne.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	@DirtiesContext
	void shouldUpdateAnExistingCashCard() {
		CashCardEntity cashCardEntity = new CashCardEntity(19.99, null);

		HttpEntity<CashCardEntity> request = new HttpEntity<>(cashCardEntity);

		ResponseEntity<Void> response = restTemplate.withBasicAuth("sarah1", "abc123").exchange("/cashcards/7",
				HttpMethod.PUT, request, Void.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

		ResponseEntity<String> getResponse = restTemplate.withBasicAuth("sarah1", "abc123")
				.getForEntity("/cashcards/13", String.class);

		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext context = JsonPath.parse(getResponse.getBody());
		Number id = context.read("$.id");
		Double amount = context.read("$.amount");

		assertThat(id).isEqualTo(7);
		assertThat(amount).isEqualTo(19.99);

	}

	@Test
	void shouldNotUpdateACashCardThatDoesNotExist() {
		CashCardEntity unknownCard = new CashCardEntity(null, 19.99, null);
		HttpEntity<CashCardEntity> request = new HttpEntity<>(unknownCard);
		ResponseEntity<Void> response = restTemplate
				.withBasicAuth("sarah1", "abc123")
				.exchange("/cashcards/99999", HttpMethod.PUT, request, Void.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	void shouldNotUpdateACashCardThatIsOwnedBySomeoneElse() {
		CashCardEntity kumarsCard = new CashCardEntity(null, 333.33, null);

		HttpEntity<CashCardEntity> request = new HttpEntity<>(kumarsCard);

		ResponseEntity<Void> response = restTemplate.withBasicAuth("sarah1", "abc123")
				.exchange("/cashcards/9", HttpMethod.PUT, request, Void.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	void shouldDeleteAnExistingCashCard() {
		ResponseEntity<Void> response = restTemplate.withBasicAuth("sarah1", "abc123")
				.exchange("/cashcards/13", HttpMethod.DELETE, null, Void.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}

	@Test
	void shouldNotDeleteACashCardThatDoesNotExist() {
		ResponseEntity<Void> deleteResponse = restTemplate
				.withBasicAuth("sarah1", "abc123")
				.exchange("/cashcards/99999", HttpMethod.DELETE, null, Void.class);
		assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	void shouldNotAllowDeletionOfCashCardsTheyDoNotOwn() {
		ResponseEntity<Void> deleteResponse = restTemplate
				.withBasicAuth("sarah1", "abc123")
				.exchange("/cashcards/9", HttpMethod.DELETE, null, Void.class);
		assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

}
