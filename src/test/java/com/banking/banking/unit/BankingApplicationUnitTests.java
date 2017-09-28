package com.banking.banking.unit;

import com.banking.banking.BankingApplication;
import com.banking.banking.model.Transaction;
import com.banking.banking.statistics.TransactionsStatistics;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * UnitTests for Transactions Statistics
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BankingApplicationUnitTests {

    @Autowired
    private BankingApplication context;

    public static final long ONE_MINUTE_AGO = System.currentTimeMillis() - TransactionsStatistics.MINUTE_IN_MILL_SECONDS;

    public static final Transaction OLD_TRANSACTION = new Transaction(3000, new Date(ONE_MINUTE_AGO));
    public static final Transaction NEW_TRANSACTION_MIN_AMOUNT = new Transaction(1000, new Date());
    public static final Transaction NEW_TRANSACTION_MAX_AMOUNT = new Transaction(4000, new Date());

    private TransactionsStatistics transactionsStatistics = TransactionsStatistics.getInstance();


    //   testing that the context is loaded
    @Test
    public void contextLoads() {
        assertThat(context).isNotNull();
    }


    /**
     * flushing the statistics before each test
     * to clear all fields.
     */
    @Before
    public void flushStatistics() {
        transactionsStatistics.resetStatistics();
    }

    /**
     * register a transaction that is older than 60 seconds
     * @result Not Registered
     */
    @Test
    public void shouldNotRegisterOldTransactions() {
        boolean registered = transactionsStatistics.registerTransaction(OLD_TRANSACTION);
        long count = transactionsStatistics.getCount();

        assertThat(registered).isEqualTo(false);
        assertThat(count).isEqualTo(0);
    }

    /**
     * register a transaction that is less than 60 seconds old
     * @result Transaction registered
     */
    @Test
    public void shouldRegisterNewTransaction() {
        boolean registered = transactionsStatistics.registerTransaction(NEW_TRANSACTION_MIN_AMOUNT);

        long count = transactionsStatistics.getCount();
        assertThat(registered).isEqualTo(true);
        assertThat(count).isEqualTo(1);
    }

    /**
     * successfully register two transactions and check the values of statistics fields
     * @resullt all valid and correct
     */
    @Test
    public void statisticsFieldsValuesShouldBeCorrect() {
        transactionsStatistics.registerTransaction(NEW_TRANSACTION_MIN_AMOUNT);
        transactionsStatistics.registerTransaction(NEW_TRANSACTION_MAX_AMOUNT);
        double sum = NEW_TRANSACTION_MIN_AMOUNT.getAmount() + NEW_TRANSACTION_MAX_AMOUNT.getAmount();
        double avg = sum / 2;
        double min = NEW_TRANSACTION_MIN_AMOUNT.getAmount();
        double max = NEW_TRANSACTION_MAX_AMOUNT.getAmount();

        long count = transactionsStatistics.getCount();

        assertThat(count).isEqualTo(2);
        assertThat(sum).isEqualTo(transactionsStatistics.getSum());
        assertThat(avg).isEqualTo(transactionsStatistics.getAvg());
        assertThat(min).isEqualTo(transactionsStatistics.getMin());
        assertThat(max).isEqualTo(transactionsStatistics.getMax());
    }

}
