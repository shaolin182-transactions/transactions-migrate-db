package org.transactions.source;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.model.transactions.Transaction;
import org.springframework.stereotype.Component;
import org.transactions.exception.MigrateDataException;
import org.transactions.factories.TransactionFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Load transactions data from a file
 */
@Component
public class TransactionsFileSource implements ITransactionsIncomeDatasource {

    private String filename;

    private BufferedReader fileReader;

    public void configure(String filename){
        this.filename = filename;
    }

    /**
     * Load a file
     */
    private void loadFile() throws MigrateDataException {
        try {
            Path path = Paths.get(filename);
            fileReader = Files.newBufferedReader(path);
        } catch (IOException e) {
            throw new MigrateDataException("Cannot load income file");
        }
    }

    @Override
    public List<Transaction> getTransactions() throws MigrateDataException {
        loadFile();

        try {
            JSONParser parser = new JSONParser();
            JSONArray json = (JSONArray) parser.parse(fileReader);
            return new TransactionFactory().buildTransactions(json);
        } catch (IOException | ParseException e) {
            throw new MigrateDataException("Cannot parse JSON file");
        }
    }
}
