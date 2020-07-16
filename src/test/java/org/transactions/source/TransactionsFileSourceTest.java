package org.transactions.source;

import org.junit.jupiter.api.Test;


class TransactionsFileSourceTest {

    @Test
    void test() throws Exception {

        TransactionsFileSource transactionsFileSource = new TransactionsFileSource();
        transactionsFileSource.configure("src/test/resources/transactions/sample_file.json");

        transactionsFileSource.getTransactions();
    }

}