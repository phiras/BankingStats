package com.banking.banking.statistics;

import com.banking.banking.model.Statistics;
import com.banking.banking.model.Transaction;

public interface TransactionService {
    boolean registerTransaction(Transaction transaction);

    Statistics getStatistics();

    void resetStatistics();
}
