package org.transactions.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.model.transactions.Transaction;
import org.model.transactions.TransactionCategory;
import org.model.transactions.TransactionCategory.TransactionCategoryBuilder;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.transactions.exception.MigrateDataException;
import org.transactions.source.ITransactionsOutcomeDatasource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ExtendWith(SpringExtension.class)
class ChangeCategoryServiceTest {

    @MockBean
    ITransactionsOutcomeDatasource mongodbDatasource;

    private List<TransactionCategory> categories;

    private List<Transaction> transactions;

    private static ResourceLoader resourceLoader = new DefaultResourceLoader();

    private static ObjectMapper mapper = new ObjectMapper();

    @Captor
    ArgumentCaptor<Transaction> transactionsCaptured;

    @BeforeEach
    public void setUp() throws IOException {

        categories = new ArrayList<>();
        categories.addAll(Stream.of(
            new TransactionCategoryBuilder().withId(1).withCategory("Category A").withLabel("Sub Category A - 1").build(),
            new TransactionCategoryBuilder().withId(2).withCategory("Category A").withLabel("Sub Category A - 2").build(),
            new TransactionCategoryBuilder().withId(3).withCategory("Category A").withLabel("Sub Category A - 3").build(),
            new TransactionCategoryBuilder().withId(4).withCategory("Category B").withLabel("Sub Category B - 1").build(),
            new TransactionCategoryBuilder().withId(5).withCategory("Category B").withLabel("Sub Category B - 1").build(),
            new TransactionCategoryBuilder().withId(17).withCategory("Category C").withLabel("Sub Category C - 1").build())
                .collect(Collectors.toList()));

        Resource resource = resourceLoader.getResource("classpath:transactions/change_category/list_multi_transactions.json");
        transactions = mapper.readValue(resource.getFile(), mapper.getTypeFactory().constructCollectionType(List.class, Transaction.class));;

    }

    @Test
    void errorOnSourceCategory() {
        Mockito.when(mongodbDatasource.getAllCategories()).thenReturn(categories);
        Assertions.assertThrows(MigrateDataException.class, () -> new ChangeCategoryService(mongodbDatasource).replace("171", "2"));
    }

    @Test
    void errorOnTargetCategory() {
        Mockito.when(mongodbDatasource.getAllCategories()).thenReturn(categories);
        Assertions.assertThrows(MigrateDataException.class, () -> new ChangeCategoryService(mongodbDatasource).replace("17", "121"));
    }

    @Test
    void nominalCase() throws MigrateDataException {

        Mockito.when(mongodbDatasource.getAllCategories()).thenReturn(categories);
        Mockito.when(mongodbDatasource.findByCategory(17)).thenReturn(transactions);

        new ChangeCategoryService(mongodbDatasource).replace("17", "2");

        Mockito.verify(mongodbDatasource, Mockito.times(3)).saveTransaction(transactionsCaptured.capture());

        List<Transaction> result = transactionsCaptured.getAllValues();

        Assertions.assertEquals(4, result.stream()
                .map(transaction -> transaction.getTransactions())
                .flatMap(List::stream)
                .filter(detail -> detail.getCategory().getId() == 2)
                .collect(Collectors.toList())
                .size());


    }
}