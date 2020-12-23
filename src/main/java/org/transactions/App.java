package org.transactions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.transactions.service.CommandDispatcherService;

@SpringBootApplication()
@EnableMongoRepositories("org.transactions.repositories")
public class App implements ApplicationRunner {

    @Autowired
    private  CommandDispatcherService dispatcherService;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        dispatcherService.run(args);
    }
}
