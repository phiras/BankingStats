package com.banking.banking.utils;

import com.banking.banking.statistics.Refreshable;
import com.banking.banking.statistics.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class StatisticsUpdaterRunner implements CommandLineRunner {
    /**
     * THIS FIELD ONLY EXPOSES THE refreshState function
     */
    @Autowired
    private Refreshable transactionService;

    @Override
    public void run(String... args) throws Exception {
//      scheduling the task of refreshing the state of the transaction service to run every 50 millSec on separate thread
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(transactionService::refreshState, 0, 50, TimeUnit.MILLISECONDS);
    }
}
