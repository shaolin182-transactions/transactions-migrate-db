package org.transactions.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Service;
import org.transactions.exception.CommandException;

import java.util.List;

/**
 * Entry point of the application
 */
@Service
public class CommandDispatcherService{

    private ChangeCategoryService categoryService;

    @Autowired
    public CommandDispatcherService(ChangeCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Interpret arguments and choose the right operation to execution
     * @param args : arguments command line
     * @throws Exception
     */
    public void run(ApplicationArguments args) throws Exception {

        // Get non option arguments
        List<String> command = args.getNonOptionArgs();
        if (command.size() != 1){
            throw new CommandException("Command line must have only 1 main argument");
        }

        String mainCommand = command.get(0);

        if (StringUtils.equalsIgnoreCase(mainCommand, CommandEnum.CHANGE_CATEGORY.toString())){
            // Get option if exists
            if (!args.containsOption("from") && !args.containsOption("to")){
                throw new CommandException("Change category command must have 'from' and 'to' options");
            }

            List<String> from = args.getOptionValues("from");
            List<String> to = args.getOptionValues("to");

            // Get associated Bean
            categoryService.replace(from.get(0), to.get(0));
        }
    }
}
