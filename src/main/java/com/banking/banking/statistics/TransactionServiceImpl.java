package com.banking.banking.statistics;

import com.banking.banking.model.Statistics;
import com.banking.banking.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.LinkedList;

/**
 * service class that retains the stat of transactions and statistics
 *
 * @author Mohamad Alaloush
 */
@Component
public class TransactionServiceImpl implements TransactionService, Refreshable {

    public static final long MINUTE_IN_MILL_SECONDS = 60000;

    private static LinkedList<Transaction> transactionsOfLastMinute = new LinkedList<Transaction>();
    private static Statistics statistics = new Statistics();

    /**
     * checks the time delta between current utc time and the transaction time
     * <p>
     * its a good clean way to clean up the controller from having more code to check if
     * the time of the transaction is valid
     *
     * @param transaction: transaction to be registered to be internally check fro validity
     * @return false if instance was older that 1 min and true if not.
     */
    public boolean registerTransaction(Transaction transaction) {
        long now = System.currentTimeMillis();
        if (now - transaction.getTimestamp().getTime() <= MINUTE_IN_MILL_SECONDS) {
            addTransaction(transaction);
            return true;
        }
        return false;
    }

    /**
     * creates a new statistics object as a copy of statistics to prevent any modification on the internal object
     *
     * @return statistics
     */
    // why do you create anew obect ?? just return the statistics object ..
    public Statistics getStatistics() {
        return new Statistics(statistics);
    }

    /**
     * resets the state of the transactionService ( useful when Testing)
     */
    public void resetStatistics() {
        synchronized (TransactionServiceImpl.class) {
            statistics.clear();
            transactionsOfLastMinute.clear();
        }
    }

    /**
     * removes expired transactions from the transactionsOfLastMinute list that are older the 1 min
     */
    public void refreshState() {
        for (int i = 0; i < transactionsOfLastMinute.size(); i++) {
            long now = System.currentTimeMillis();
            if (now - transactionsOfLastMinute.get(i).getTimestamp().getTime() > MINUTE_IN_MILL_SECONDS) {
                synchronized (TransactionServiceImpl.class) {
                    double amount = transactionsOfLastMinute.remove(i).getAmount();
                    removeAmount(amount);
                }
            }
        }
    }

    /**
     * adds a transaction and calculates the new status of the statistics related fields and updates them
     *
     * @param transaction: transaction to add to transactionsList
     */

    private void addTransaction(Transaction transaction) {
        synchronized (TransactionServiceImpl.class) {
            double amount = transaction.getAmount();
            transactionsOfLastMinute.addFirst(transaction);
            addAmount(amount);
        }
    }


    /**
     * calculates the new status of the statistics and updates it after removing a transaction
     *
     * @param amount : the amount of the deleted transaction
     */
    private void removeAmount(double amount) {
        long newCount = statistics.getCount() - 1;
        statistics.setCount(newCount);

        double newSum = statistics.getSum() - amount;
        statistics.setSum(newSum);
        if (newCount == 0) {
            statistics.setAvg(0);
        } else {
            double newAvg = newSum / newCount;
            statistics.setAvg(newAvg);
        }
        if (statistics.getMax() == amount) {
            double newMax = getAmountOfMaxTransaction();
            statistics.setMax(newMax);
        }
        if (statistics.getMin() == amount) {
            if (newCount == 0) {
                statistics.setMin(0);
            } else {
                double newMin = getAmountOfMinTransaction();
                statistics.setMin(newMin);
            }
        }
    }

    /**
     * calculates the new status of the statistics and updates it after adding an a transaction
     *
     * @param amount : the amount of the deleted transaction
     */
    private void addAmount(double amount) {
        long newCount = statistics.getCount() + 1;
        double newSum = statistics.getSum() + amount;
        double newAvg = newSum / newCount;
        if (amount > statistics.getMax())
            statistics.setMax(amount);
        if (amount < statistics.getMin() || statistics.getMin() == 0)
            statistics.setMin(amount);
        statistics.setAvg(newAvg);
        statistics.setCount(newCount);
        statistics.setSum(newSum);
    }


    /**
     * convenience method to get the Transaction with the highest amount value
     *
     * @return double: the amount from the highest transaction
     */
    private double getAmountOfMaxTransaction() {
        double maxAmount = 0;
        for (Transaction transaction : transactionsOfLastMinute) {
            double amount = transaction.getAmount();
            if (maxAmount < amount)
                maxAmount = amount;
        }
        return maxAmount;
    }

    /**
     * convenience method to get the Transaction with the lowest amount value
     *
     * @return double: minimum amount of transaction from transactions
     */
    private double getAmountOfMinTransaction() {
        double minAmount = Double.POSITIVE_INFINITY;
        for (Transaction transaction : transactionsOfLastMinute) {
            double amount = transaction.getAmount();
            if (minAmount > amount)
                minAmount = amount;
        }
        return minAmount;
    }


}
