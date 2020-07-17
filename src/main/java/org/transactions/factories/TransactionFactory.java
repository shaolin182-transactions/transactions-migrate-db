package org.transactions.factories;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.model.transactions.BankAccount;
import org.model.transactions.Transaction;
import org.model.transactions.TransactionCategory;
import org.model.transactions.TransactionDetails;
import org.model.transactions.builder.TransactionBuilder;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionFactory {

    private static final Logger LOGGER = LogManager.getLogger();

    public List<Transaction> buildTransactions(JSONArray inputData){
        inputData.stream().map(item -> buildTransaction((JSONObject) item)).collect(Collectors.toList());
        return (List<Transaction>) inputData.stream().map(item -> buildTransaction((JSONObject) item)).collect(Collectors.toList());
    }

    /**
     * Build a transaction object from a json object
     *
     * @param item : current object to translate into Transaction
     * @return a Transaction object
     */
    Transaction buildTransaction(JSONObject item) {
        try {
            TransactionBuilder builder = new TransactionBuilder();

            builder.withDate(OffsetDateTime.parse(item.get("date").toString()));
            builder.withCost(Long.valueOf(item.get("cost").toString()));
            builder.withBankAccountTo(buildBankAccount(item));
            builder.addTransactions(buildTransactionsList(item));

            return builder.build();
        } catch (Exception e) {
            // On any exception, log error with maximum context info for debugging purpose
            LOGGER.atError().withThrowable(e).log("Cannot migrate transactions {}", item);
        }

        return null;
    }

    /**
     * Build a bank account object from a Json object
     * @param item : current json data
     */
    private BankAccount buildBankAccount(JSONObject item){
        JSONObject bankAccount = (JSONObject) item.get("bankaccount");
        if (bankAccount != null){
            BankAccount.BankAccountBuilder builder = new BankAccount.BankAccountBuilder();
            builder.withId(Integer.valueOf(bankAccount.get("id").toString()));
            builder.withLabel(bankAccount.get("label").toString());
            builder.withCategory(bankAccount.get("category").toString());

            return builder.build();
        }
        return null;
    }

    /**
     * Build a category object from a Json object
     * @param item : current json data
     */
    private TransactionCategory buildTransactionCategory(JSONObject item){
        JSONObject category = (JSONObject) item.get("category");
        if (category != null){
            TransactionCategory.TransactionCategoryBuilder builder = new TransactionCategory.TransactionCategoryBuilder();
            builder.withId(Integer.valueOf(category.get("id").toString()));
            builder.withLabel(category.get("label").toString());
            builder.withCategory(category.get("category").toString());

            return builder.build();
        }
        return null;
    }

    /**
     * Build a transaction list from Json data
     * @param item : current json data
     * @return a list of transactions
     */
    private List<TransactionDetails> buildTransactionsList(JSONObject item) {

        Boolean multi = (Boolean) item.get("multi");
        List<TransactionDetails> result = new ArrayList<>();

        if (BooleanUtils.isTrue(multi)) {
            JSONArray subTransactions = (JSONArray) item.get("subtransaction");
            result = (List<TransactionDetails>) subTransactions.stream().map(elt -> buildTransactionDetail((JSONObject) elt)).collect(Collectors.toList());
        } else {
            result.add(buildTransactionDetail(item));
        }

        return result;
    }

    /**
     * Build a transaction detail object
     * @param item : current item
     * @return a TransactionDetails object
     */
    private TransactionDetails buildTransactionDetail(JSONObject item) {

        String label = null;
        if (item.get("label") != null) {
            label = (String) item.get("label");
        }

        TransactionDetails.TransactionDetailsBuilder builder = new TransactionDetails.TransactionDetailsBuilder();
        return builder.withCategory(buildTransactionCategory(item))
                .withDescription(label)
                .withIncome(Float.valueOf(item.get("income").toString()))
                .withOutcome(Float.valueOf(item.get("outcome").toString()))
                .build();
    }
}
