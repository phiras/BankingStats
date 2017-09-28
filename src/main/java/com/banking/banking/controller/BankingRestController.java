package com.banking.banking.controller;

import com.banking.banking.statistics.TransactionsStatistics;
import com.banking.banking.model.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * the main controller
 *
 * @author Mohamad Alaloush
 */
@RestController
public class BankingRestController {

//  to make sure that one and only one instance exists at all times across all threads
//  we make a heavy use of the thread save singleton design pattern
    private TransactionsStatistics transactionsStatistics = TransactionsStatistics.getInstance();

    /**
     * method that automatically picks up the json transaction from the
     *  request body and creates an object of Transaction
     *  and replies with status 201 if the transaction was registered
     *                      or  204 if the transaction wasn't
     * @param transaction
     */
    @RequestMapping(method = RequestMethod.POST,
                    produces = "application/json", path = "/transactions")
    public final void saveTransaction(@RequestBody Transaction transaction, HttpServletResponse response) {
//      having the registerTransaction method helps keeping our controller more clean
        boolean registered = transactionsStatistics.registerTransaction(transaction);

        if (registered) {
            response.setStatus(HttpStatus.CREATED.value());

        } else {
            response.setStatus(HttpStatus.NO_CONTENT.value());
        }
    }

    @RequestMapping(path = "/statistics", produces = "application/json")
    public final TransactionsStatistics getTransactionsStatistics() {
        return transactionsStatistics;
    }
}
