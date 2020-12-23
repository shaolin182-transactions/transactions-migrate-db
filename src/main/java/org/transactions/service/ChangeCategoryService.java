package org.transactions.service;

import org.model.transactions.Transaction;
import org.model.transactions.TransactionCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.transactions.exception.MigrateDataException;
import org.transactions.source.ITransactionsOutcomeDatasource;

import java.util.List;

@Service("ChangeCategoryService")
public class ChangeCategoryService {

    private final ITransactionsOutcomeDatasource transactionsDatasource;

    @Autowired
    public ChangeCategoryService(ITransactionsOutcomeDatasource transactionsDatasource){
        this.transactionsDatasource = transactionsDatasource;
    }

    /**
     * Look into all transactions and replace 'from' category' by 'to category'
     * @param from
     * @param to
     */
    public void replace(String from, String to) throws MigrateDataException {

        Integer fromCategory = Integer.valueOf(from);
        Integer toCategory = Integer.valueOf(to);

        // Get all existing categories
        List<TransactionCategory> categories = transactionsDatasource.getAllCategories();

        // Check category existence
        categories.stream()
                .filter(cat -> cat.getId() == fromCategory)
                .findFirst().orElseThrow(() -> new MigrateDataException(String.format("Source Category %s was not found", from)));

        TransactionCategory targetCategory = categories.stream()
                .filter(cat -> cat.getId() == toCategory)
                .findFirst().orElseThrow(() -> new MigrateDataException(String.format("Target Category %s was not found", from)));;

        // Get all transactions concerned about source category
        List<Transaction> transactions = transactionsDatasource.findByCategory(fromCategory);

        // Replace transactions
        transactions.forEach(transaction -> replace(transaction, targetCategory, fromCategory));

        // Save transactions
        transactions.forEach(transaction -> transactionsDatasource.saveTransaction(transaction));
    }

    /**
     * Replace categories on all sub transactions where its category matches fromCategory
     * @param transaction : current transaction
     * @param targetCategory : target category
     * @param fromCategory : id of the source category we want to replace
     */
    private void replace(Transaction transaction, TransactionCategory targetCategory, Integer fromCategory){
        transaction.getTransactions().stream()
                .filter(sub -> sub.getCategory().getId() == fromCategory)
                .forEach(sub -> sub.setCategory(targetCategory));
    }
}
