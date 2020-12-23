package org.transactions.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class CommandDispatcherServiceTest {

    @MockBean
    ChangeCategoryService categoryService;

    @Test
    void testCommandDispatcher() throws Exception {
        new CommandDispatcherService(categoryService).run(new DefaultApplicationArguments(new String[]{"CHANGE_CATEGORY"}));
    }

}