package org.transactions.source;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.model.transactions.Transaction;

import java.util.List;


class TransactionsFileSourceTest {

    @Test
    @DisplayName("Build transactions from a JSON file - Nominal case")
    void nominalCase() throws Exception {

        TransactionsFileSource transactionsFileSource = new TransactionsFileSource();
        transactionsFileSource.configure("src/test/resources/transactions/sample_file.json");

        List<Transaction> result = transactionsFileSource.getTransactions();

        MatcherAssert.assertThat(result, Matchers.hasSize(218));
    }

}