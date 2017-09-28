package com.banking.banking.integration;

import com.banking.banking.model.Transaction;
import com.banking.banking.statistics.TransactionsStatistics;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration Test for Testing that the StatisticsUpdater is working properly
 *
 * @author Mohamad Alaloush
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UpdaterIntegrationTests {

    //    public static final Transaction NEW_TRANSACTION_MAX_AMOUNT = new Transaction(4000, new Date());
    public static final Transaction NEW_TRANSACTION_MIN_AMOUNT = new Transaction(1000, new Date());

    private TransactionsStatistics transactionsStatistics = TransactionsStatistics.getInstance();

    /**
     * successfully register two transactions:
     * 1- one is valid for 60 seconds
     * 2- the other is valid for 5 seconds only ( to make this test faster so it doesn't have to wait 60 seconds )
     *
     * @throws InterruptedException
     * @result after waiting for 6 seconds, only one of the transactions ( transaction 2 ) was deleted
     */
    @Test
    public void shouldRemoveTransactionsAfterExpiration() throws InterruptedException {
//      getting the UTC time in milliseconds 55 seconds ago
        long fiftyFiveSecondsAgo = System.currentTimeMillis() - 55000;
        Transaction transactionExpiresIn5Seconds = new Transaction(2000, new Date(fiftyFiveSecondsAgo));

        transactionsStatistics.registerTransaction(transactionExpiresIn5Seconds);
        transactionsStatistics.registerTransaction(NEW_TRANSACTION_MIN_AMOUNT);

        long countBefore = transactionsStatistics.getCount();
//      waiting to for the transaction 2 to be deleted
        TimeUnit.SECONDS.sleep(6);

        long countAfter = transactionsStatistics.getCount();
        assertThat(countBefore).isEqualTo(countAfter + 1);
    }


}
