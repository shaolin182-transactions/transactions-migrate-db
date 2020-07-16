package org.transactions;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories("org.transactions.repositories")
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
