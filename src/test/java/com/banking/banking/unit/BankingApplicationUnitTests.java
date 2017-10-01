package com.banking.banking.unit;

import com.banking.banking.BankingApplication;
import com.banking.banking.model.Transaction;
import com.banking.banking.statistics.TransactionServiceImpl;
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

    private static final long ONE_MINUTE_AGO = System.currentTimeMillis() - TransactionServiceImpl.MINUTE_IN_MILL_SECONDS;

    private static final Transaction OLD_TRANSACTION = new Transaction(3000, new Date(ONE_MINUTE_AGO));
    private static final Transaction NEW_TRANSACTION_MIN_AMOUNT = new Transaction(1000, new Date());
    private static final Transaction NEW_TRANSACTION_MAX_AMOUNT = new Transaction(4000, new Date());

    @Autowired
    private TransactionServiceImpl transactionServiceImpl;


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
        transactionServiceImpl.resetStatistics();
    }

    /**
     * register a transaction that is older than 60 seconds
     * @result Not Registered
     */
    @Test
    public void When_RegisteringTransactionMoreThan60SecOld_ShouldNot_BeRegistered() {
        boolean registered = transactionServiceImpl.registerTransaction(OLD_TRANSACTION);
        long count = transactionServiceImpl.getStatistics().getCount();

        assertThat(registered).isEqualTo(false);
        assertThat(count).isEqualTo(0);
    }
    /**
     * register a transaction that is less than 60 seconds old
     * @result Transaction registered
     */
    @Test
    public void When_RegisteringTransactionLessThan60SecOld_Should_BeRegistered() {
        boolean registered = transactionServiceImpl.registerTransaction(NEW_TRANSACTION_MIN_AMOUNT);

        long count = transactionServiceImpl.getStatistics().getCount();
        assertThat(registered).isEqualTo(true);
        assertThat(count).isEqualTo(1);
    }

    /**
     * successfully register two transactions and check the values of statistics fields
     * @resullt all valid and correct
     */
    @Test
    public void When_AddingTwoValidTransactions_Expect_CorrectStatistics() {
        transactionServiceImpl.registerTransaction(NEW_TRANSACTION_MIN_AMOUNT);
        transactionServiceImpl.registerTransaction(NEW_TRANSACTION_MAX_AMOUNT);
        double sum = NEW_TRANSACTION_MIN_AMOUNT.getAmount() + NEW_TRANSACTION_MAX_AMOUNT.getAmount();
        double avg = sum / 2;
        double min = NEW_TRANSACTION_MIN_AMOUNT.getAmount();
        double max = NEW_TRANSACTION_MAX_AMOUNT.getAmount();

        long count = transactionServiceImpl.getStatistics().getCount();

        assertThat(count).isEqualTo(2);
        assertThat(sum).isEqualTo(transactionServiceImpl.getStatistics().getSum());
        assertThat(avg).isEqualTo(transactionServiceImpl.getStatistics().getAvg());
        assertThat(min).isEqualTo(transactionServiceImpl.getStatistics().getMin());
        assertThat(max).isEqualTo(transactionServiceImpl.getStatistics().getMax());
    }

}
