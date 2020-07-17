package org.transactions.factories;


import org.hamcrest.MatcherAssert;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.model.transactions.Transaction;
import org.model.transactions.TransactionDetails;
import org.model.transactions.TransactionDetails.TransactionDetailsBuilder;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Building transactions from JSON")
class TransactionFactoryTest {

    @Test
    @DisplayName("Nominal case")
    void buildTransaction() {
        JSONObject sampleData = new JSONObject();
        sampleData.put("_id", "59ac2e5e0f5eb2296a1dacdd");
        sampleData.put("date", "2011-01-01T00:00:00.000Z");
        sampleData.put("income", 19.66);
        sampleData.put("outcome", 0);
        sampleData.put("label", "Entered automatically by YNAB");
        sampleData.put("cost", "1966");

        JSONObject sampleCategory = new JSONObject();
        sampleCategory.put("id", 45);
        sampleCategory.put("category", "Revenu");
        sampleCategory.put("label", "Salaire");
        sampleData.put("category", sampleCategory);

        JSONObject sampleBankAccount = new JSONObject();
        sampleBankAccount.put("id", 46);
        sampleBankAccount.put("category", "Perso");
        sampleBankAccount.put("label", "Espèces");
        sampleData.put("bankaccount", sampleBankAccount);

        Transaction result = new TransactionFactory().buildTransaction(sampleData);

        Assertions.assertAll(
                () -> assertEquals(1966, result.getCost(), "Error on cost"),
                () -> assertEquals(OffsetDateTime.of(2011, 1, 1, 0, 0, 0,0, ZoneOffset.UTC), result.getDate(), "Error on create date time"),
                () -> assertEquals(46, result.getTo().getId(), "Error on bank account id"),
                () -> assertEquals("Espèces", result.getTo().getLabel(), "Error on bank account label"),
                () -> assertEquals("Perso", result.getTo().getCategory(), "Error on bank account category"),
                () -> assertEquals(45, result.getTransactions().get(0).getCategory().getId(), "Error on category id"),
                () -> assertEquals("Salaire", result.getTransactions().get(0).getCategory().getLabel(), "Error on category label"),
                () -> assertEquals("Revenu", result.getTransactions().get(0).getCategory().getCategory(), "Error on vategory category"),
                () -> assertEquals(19.66f, result.getTransactions().get(0).getIncome(), "Error on income"),
                () -> assertEquals(0f, result.getTransactions().get(0).getOutcome(), "Error on outcome"),
                () -> assertEquals("Entered automatically by YNAB", result.getTransactions().get(0).getDescription(), "Error on bank account category")
        );
    }

    @Test
    @DisplayName("No transaction category")
    void buildTransactionWithNoCategory() {
        JSONObject sampleData = new JSONObject();
        sampleData.put("_id", "59ac2e5e0f5eb2296a1dacdd");
        sampleData.put("date", "2011-01-01T00:00:00.000Z");
        sampleData.put("income", 19.66);
        sampleData.put("outcome", 0);
        sampleData.put("label", "Entered automatically by YNAB");
        sampleData.put("cost", "1966");

        JSONObject sampleBankAccount = new JSONObject();
        sampleBankAccount.put("id", 46);
        sampleBankAccount.put("category", "Perso");
        sampleBankAccount.put("label", "Espèces");
        sampleData.put("bankaccount", sampleBankAccount);

        Transaction result = new TransactionFactory().buildTransaction(sampleData);

        Assertions.assertAll(
                () -> assertEquals(1966, result.getCost(), "Error on cost"),
                () -> assertEquals(OffsetDateTime.of(2011, 1, 1, 0, 0, 0,0, ZoneOffset.UTC), result.getDate(), "Error on create date time"),
                () -> assertEquals(46, result.getTo().getId(), "Error on bank account id"),
                () -> assertEquals("Espèces", result.getTo().getLabel(), "Error on bank account label"),
                () -> assertEquals("Perso", result.getTo().getCategory(), "Error on bank account category"),
                () -> assertEquals(19.66f, result.getTransactions().get(0).getIncome(), "Error on income"),
                () -> assertEquals(0f, result.getTransactions().get(0).getOutcome(), "Error on outcome"),
                () -> assertEquals("Entered automatically by YNAB", result.getTransactions().get(0).getDescription(), "Error on bank account category")
        );
    }

    @Test
    @DisplayName("Multi Transaction")
    void buildMultiTransaction() throws ParseException {

        String jsonData = "{\n" +
                "    \"_id\": \"59d10dc33f696f13af22a88c\",\n" +
                "    \"income\": 0,\n" +
                "    \"outcome\": 266,\n" +
                "    \"date\": \"2017-09-17T15:44:10.000Z\",\n" +
                "    \"multi\": true,\n" +
                "    \"subtransaction\": [\n" +
                "      {\n" +
                "        \"income\": 0,\n" +
                "        \"outcome\": 337.5,\n" +
                "        \"category\": {\n" +
                "          \"id\": 63,\n" +
                "          \"category\": \"Vacances\",\n" +
                "          \"label\": \"2017 - Sud France\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"income\": 0,\n" +
                "        \"outcome\": 75,\n" +
                "        \"category\": {\n" +
                "          \"id\": 25,\n" +
                "          \"category\": \"Maison\",\n" +
                "          \"label\": \"Meuble/Electroménager\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"income\": 69,\n" +
                "        \"outcome\": 0,\n" +
                "        \"category\": {\n" +
                "          \"id\": 27,\n" +
                "          \"category\": \"Loisir\",\n" +
                "          \"label\": \"Sorties\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"income\": 50,\n" +
                "        \"outcome\": 0,\n" +
                "        \"category\": {\n" +
                "          \"id\": 13,\n" +
                "          \"category\": \"Loisir\",\n" +
                "          \"label\": \"Jeux d’argent\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"income\": 27.5,\n" +
                "        \"outcome\": 0,\n" +
                "        \"category\": {\n" +
                "          \"id\": 40,\n" +
                "          \"category\": \"Loisir\",\n" +
                "          \"label\": \"PC\"\n" +
                "        }\n" +
                "      }\n" +
                "    ],\n" +
                "    \"category\": {\n" +
                "      \"id\": 0,\n" +
                "      \"category\": \"Multi\",\n" +
                "      \"label\": \"Multi\"\n" +
                "    },\n" +
                "    \"label\": \"Rbt Sandra\",\n" +
                "    \"bankaccount\": {\n" +
                "      \"id\": 26,\n" +
                "      \"category\": \"Perso\",\n" +
                "      \"label\": \"ING Direct\"\n" +
                "    },\n" +
                "    \"cost\": -26600\n" +
                "  }";
        JSONParser parser = new JSONParser();
        JSONObject sampleData = (JSONObject) parser.parse(jsonData);

        Transaction result = new TransactionFactory().buildTransaction(sampleData);

        Assertions.assertAll(
                () -> assertEquals(-26600, result.getCost(), "Error on cost"),
                () -> assertEquals(OffsetDateTime.of(2017, 9, 17, 15, 44, 10,0, ZoneOffset.UTC), result.getDate(), "Error on create date time"),
                () -> assertEquals(26, result.getTo().getId(), "Error on bank account id"),
                () -> assertEquals("ING Direct", result.getTo().getLabel(), "Error on bank account label"),
                () -> assertEquals("Perso", result.getTo().getCategory(), "Error on bank account category"),
                () -> assertEquals(0f, result.getTransactions().get(0).getIncome(), "Error on income"),
                () -> assertEquals(337.5f, result.getTransactions().get(0).getOutcome(), "Error on outcome")
        );

        TransactionDetails detail = new TransactionDetailsBuilder()
                .withIncome(27.5f).withOutcome(0f)
                .withCategory().withId(40).withCategory("Loisir").withLabel("PC").done()
                .build();
        MatcherAssert.assertThat(detail, is(in(result.getTransactions())));

    }


}