package org.transactions.repositories;

import org.model.transactions.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransactionsRepository extends MongoRepository<Transaction, String>  {
}
