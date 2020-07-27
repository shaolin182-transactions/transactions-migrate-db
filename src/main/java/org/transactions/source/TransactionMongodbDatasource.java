package org.transactions.source;

import org.model.transactions.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.transactions.persistence.repositories.TransactionsRepository;

@Component
public class TransactionMongodbDatasource implements ITransactionsOutcomeDatasource {

    @Autowired
    private TransactionsRepository repository;

    @Override
    public void saveTransaction(Transaction aTransaction) {
        repository.save(aTransaction);
    }
}
