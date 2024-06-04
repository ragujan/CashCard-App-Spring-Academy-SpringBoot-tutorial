package com.springAcademy.familyCashCardApp;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

@JsonTest
public class CashCardJsonTest {
	
	@Autowired
	private JacksonTester<CashCardRecord> json;
  
	@Test
	 void firstJsonTest() {
		assertThat(1).isEqualTo(1);
	}
	
	@Test
	void cashCardSerializationTest() throws IOException{
		CashCardRecord cashCard = new CashCardRecord(99L,123.45);
		assertThat(json.write(cashCard)).isStrictlyEqualToJson("expected.json");
		assertThat(json.write(cashCard)).hasJsonPathNumberValue("@.id");
		assertThat(json.write(cashCard)).extractingJsonPathNumberValue("@.id");
		assertThat(json.write(cashCard)).extractingJsonPathNumberValue("@.id").isEqualTo(99);
		assertThat(json.write(cashCard)).hasJsonPathNumberValue("@.amount");
		assertThat(json.write(cashCard)).extractingJsonPathNumberValue("@.amount").isEqualTo(123.45);
	}
	
	@Test
	void cashCardDeserialization() throws IOException{
		   String expected = """
		           {
		               "id":99,
		               "amount":123.45
		           }
		           """;
		   assertThat(json.parseObject(expected).id()).isEqualTo(99);
		   assertThat(json.parseObject(expected).amount()).isEqualTo(123.45);
	}
}
