package com.banking.banking.utils;

import com.banking.banking.statistics.TransactionsStatistics;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * very simple thread class
 * @author Mohamad Alaloush
 */
public class StatisticsUpdater extends Thread {

    TransactionsStatistics transactionsStatistics = TransactionsStatistics.getInstance() ;

    @Override
    public void run() {
        transactionsStatistics.refreshState();
    }
}
