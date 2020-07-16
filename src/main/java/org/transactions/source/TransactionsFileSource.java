package org.transactions.source;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
    private void loadFile() throws IOException {
        Path path = Paths.get(filename);
        fileReader = Files.newBufferedReader(path);
    }

    @Override
    public List<Object> getTransactions() throws Exception {
        loadFile();

        JSONParser parser = new JSONParser();
        JSONArray json = (JSONArray) parser.parse(fileReader);

        return json;
    }
}
