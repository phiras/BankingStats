package com.banking.banking.controller;

import com.banking.banking.model.Statistics;
import com.banking.banking.model.Transaction;
import com.banking.banking.statistics.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private TransactionService transactionService;

    /**
     * method that automatically picks up the json transaction from the
     * request body and creates an object of Transaction
     * and replies with status 201 if the transaction was registered
     * or  204 if the transaction wasn't
     *
     * @param transaction
     */
    @RequestMapping(method = RequestMethod.POST, path = "/transactions", produces = "application/json")
    public final void saveTransaction(@RequestBody Transaction transaction, HttpServletResponse response) {
//      having the registerTransaction method helps keeping our controller more clean
        boolean registered = transactionService.registerTransaction(transaction);

        if (registered) {
            response.setStatus(HttpStatus.CREATED.value());

        } else {
            response.setStatus(HttpStatus.NO_CONTENT.value());
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "/statistics", produces = "application/json")
    public final Statistics getTransactionServiceImpl() {
        return transactionService.getStatistics();
    }
}
