package org.transactions.source;

import org.model.transactions.Transaction;

public interface ITransactionsOutcomeDatasource {

    void saveTransaction(Transaction transaction);
}
