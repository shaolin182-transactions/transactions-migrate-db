package org.transactions.repositories;

import org.model.transactions.Transaction;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import org.transactions.persistence.repositories.TransactionsRepository;

import java.util.List;

@Repository
public interface TransactionsMigrationRepository extends TransactionsRepository {

    @Query("{ 'transactions.category.id' : ?0 }")
    List<Transaction> findByCategoryId(Integer categoryId);
}
