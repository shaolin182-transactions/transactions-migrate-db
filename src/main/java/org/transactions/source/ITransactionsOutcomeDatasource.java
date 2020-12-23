package org.transactions.source;

import org.model.transactions.Transaction;
import org.model.transactions.TransactionCategory;

import java.util.List;

public interface ITransactionsOutcomeDatasource {

    void saveTransaction(Transaction transaction);

    List<Transaction> findByCategory(Integer categoryId);

    List<TransactionCategory> getAllCategories();
}
