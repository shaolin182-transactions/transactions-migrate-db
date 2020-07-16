package org.transactions.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.model.transactions.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.transactions.source.ITransactionsIncomeDatasource;
import org.transactions.source.ITransactionsOutcomeDatasource;

import java.util.List;

@Component
public class MigrateDataService implements CommandLineRunner {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    ITransactionsIncomeDatasource fromDatasource;

    @Autowired
    ITransactionsOutcomeDatasource toDatasource;

    @Override
    public void run(String... args) throws Exception {
        if (args.length == 1){
            // Get Filename
        } else {
            LOGGER.atError().log("Error - Wrong arguments");
            throw new RuntimeException("Error - Wrong arguments");
        }

        List<Object> dataToMigrate = fromDatasource.getTransactions();

        dataToMigrate.forEach(item -> toDatasource.saveTransaction((Transaction) item));
    }
}
