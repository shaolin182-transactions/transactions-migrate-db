package org.transactions.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.model.transactions.Transaction;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Identifies transactions that are related
 */
public class IdentifyTransferService {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final String MERGED_FLAG_TO_DELETE = "DO NOT CREATE - TRANSFER TRANSACTION MERGED";

    private static final String MERGED_FLAG_TO_CREATE = "CREATE - TRANSFER TRANSACTION MERGED";

    /**
     * Identify that 2 transactions are related or not
     *
     * In our case, 2 transactions are related if :
     * - they do not have a category property
     * - both transactions has same cost
     * - both transactions are in the same day
     * @param transactions : the list of all transactions
     */
    public void identifyTransferTransactions(List<Transaction> transactions) {
        List<Transaction> transferTransactions = transactions.stream()
                .filter(transaction -> isTransferTransaction(transaction))
                .collect(Collectors.toList());

        transferTransactions.forEach(origin -> lookForOtherPart(origin, transferTransactions));
    }

    /**
     * Look for other part of the transaction
     * @param origin
     * @param transactions
     */
    private void lookForOtherPart(Transaction origin, List<Transaction> transactions) {
        List<Transaction> matchTransaction = transactions.stream()
                .filter(item -> matchTransactionTransfer(item, origin))
                .collect(Collectors.toList());

        if (matchTransaction.size() != 1){
            // Log an error
            LOGGER.atError().log("No match found for this transfer transaction {}", origin);
        } else {
            // merge transactions
            mergeTransaction(origin, matchTransaction.get(0));
        }
    }

    private void mergeTransaction(Transaction origin, Transaction duplicate) {
        LOGGER.atInfo().log("Merge those transactions into one origin: {} , current: {}", origin, duplicate);
        duplicate.setId(MERGED_FLAG_TO_DELETE);
        origin.setId(MERGED_FLAG_TO_CREATE);

        // todo : comment faire la somme des comptes sur ce type de transaction
        // puisque le cost ne sera pour qu'un seul compte

        /**
         * L'idée était de ne pas compter les transactions quand elles sont liées à des virements entre compte
         *
         * Auparavant, un virement de 100€ entre Livret A et LDD amenait une déponse de 500€ d'un côté puis une entrée de 100€ de l'autre ce qui faussait en partie les calculs
         * Si on veut les exclure
         *
         */

    }

    /**
     * Identify if both transactions concerns same transfer
     *  - same cost
     *  - same date
     *  - not already merged
     * @param current : current transaction
     * @param origin : the original transaction
     * @return true if criteria matches, false otherwise
     */
    private boolean matchTransactionTransfer(Transaction current, Transaction origin) {
        if (current.getCost() + origin.getCost() == 0
                && ChronoUnit.DAYS.between(current.getDate(), origin.getDate()) <= 1
                && !StringUtils.equals(current.getId(), MERGED_FLAG_TO_DELETE)
                && !StringUtils.equals(current.getId(), MERGED_FLAG_TO_CREATE)){
            return true;
        }
        return false;
    }

    /**
     * Check if this transaction is part of a transfer transaction, means no category
     * @param transaction : current transaction
     * @return true, if the transaction is a transfer
     */
    public Boolean isTransferTransaction(Transaction transaction) {
        return transaction.getTransactions().stream().noneMatch(item -> item.getCategory() != null);
    }
}
