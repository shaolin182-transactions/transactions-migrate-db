package org.transactions.source;

import org.model.transactions.Transaction;
import org.transactions.exception.MigrateDataException;

import java.util.List;

public interface ITransactionsIncomeDatasource {

    List<Transaction> getTransactions() throws MigrateDataException;
}
