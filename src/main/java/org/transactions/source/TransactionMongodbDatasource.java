package org.transactions.source;

import org.model.transactions.Transaction;
import org.model.transactions.TransactionCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.transactions.repositories.TransactionsMigrationRepository;

import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionMongodbDatasource implements ITransactionsOutcomeDatasource {

    @Autowired
    private TransactionsMigrationRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void saveTransaction(Transaction aTransaction) {
        repository.save(aTransaction);
    }

    @Override
    public List<Transaction> findByCategory(Integer categoryId) {
        return repository.findByCategoryId(categoryId);
    }

    @Override
    public List<TransactionCategory> getAllCategories() {
        return mongoTemplate.getCollection("transaction")
                .distinct("transactions.category", TransactionCategory.class)
                .into(new ArrayList<>());
    }
}
