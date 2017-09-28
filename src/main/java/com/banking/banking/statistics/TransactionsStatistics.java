package com.banking.banking.statistics;

import com.banking.banking.model.Transaction;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * A Singleton-ThreadSafe class that contains the stat of transactions statistics of the last min
 *
 * using the singleton design pattern One cannot use the constructor but the static method getInstance
 *
 * with @XmoRootElement annotation and having getters only for the statistics related fields, spring will automatically
 *  include only these fields
 *
 * @author Mohamad Alaloush
 */
@XmlRootElement
public class TransactionsStatistics {

    private static TransactionsStatistics instance;

    public static final long MINUTE_IN_MILL_SECONDS = 60000;

    private List<Transaction> transactionsOfLastMinute;

    //  statistics related fields
    private double sum;
    private double avg;
    private double max;
    private double min;
    private long count;

    private TransactionsStatistics() {
        this.sum = 0;
        this.count = 0;
        this.avg = 0;
        this.max = 0;
        this.min = 0;
        this.transactionsOfLastMinute = new ArrayList<>();
    }
//  =========== GETTERS ONLY FOR THE STATISTICS RELATED FIELDS ========
    public double getSum() {
        return sum;
    }

    public double getAvg() {
        return avg;
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }

    public long getCount() {
        return count;
    }

    public  synchronized void resetStatistics(){
        this.sum = 0;
        this.count = 0;
        this.avg = 0;
        this.max = 0;
        this.min = 0;
        this.transactionsOfLastMinute = new ArrayList<>();
    }
    /**
     * if no instance of TransactionStatistics exists it will create new one
     * otherwise it will return the existing instance
     *
     * @return an instance of TransactionsStatistics
     */
    public static synchronized TransactionsStatistics getInstance() {
        if (instance == null) {
            instance = new TransactionsStatistics();
        }
        return instance;
    }

    /**
     * checks the time delta between current utc time and the transaction time
     * <p>
     * its a good clean way to clean up the controller from having more code to check if
     * the time of the transaction is valid
     *
     * @param transaction transaction to be registered to be internally check fro validity
     * @return false if instance was older that 1 min and true if not.
     */
    public synchronized boolean registerTransaction(Transaction transaction) {
        long now = System.currentTimeMillis();
//      TODO : remove this print out
        if (now - transaction.getTimestamp().getTime() <= MINUTE_IN_MILL_SECONDS) {
            addTransaction(transaction);
            return true;
        }
        return false;
    }

    /**
     * removes expired transactions from the transactionsOfLastMinute list that are older the 1 min
     */
    public synchronized void refreshState() {
        long now = System.currentTimeMillis();
        for (int i = 0; i < transactionsOfLastMinute.size(); i++) {
            if (now - transactionsOfLastMinute.get(i).getTimestamp().getTime() > MINUTE_IN_MILL_SECONDS) {
                double amount = transactionsOfLastMinute.remove(i).getAmount();
                updateStateAfterRemovingTransaction(amount);
            }

        }
    }

    /**
     * adds a transaction and calculates the new status of the statistics related fields and updates them
     *
     * @param transaction transaction to add to transactionsList
     */
    private synchronized void addTransaction(Transaction transaction) {
        transactionsOfLastMinute.add(transaction);

        double amount = transaction.getAmount();
        count += 1;
        sum = sum + amount;
        avg = sum / count;
        if (amount > max)
            max = amount;
        if (amount < min || min == 0)
            min = amount;
    }

    /**
     * removes a transaction and calculates the new status of the statistics related fields and updates them
     *
     * @param amount the amount of the deleted transaction
     */
    private synchronized void updateStateAfterRemovingTransaction(double amount) {
        count -= 1;
        sum = sum - amount;
        if (count != 0)
            avg = sum / count;

        if (count == 0)
            avg = 0;
        if (max == amount)
            max = getMaxTransaction();

        if (min == amount) {
            if (count == 0) {
                min = 0;
            } else {
                min = getMinTransaction();
            }
        }
    }

    /**
     * convenience method to get the Transaction with the highest amount value
     *
     * @return double the amount from the highest transaction
     */
    private synchronized double getMaxTransaction() {
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
     * @return minimum amount of transaction from transactions
     */
    private synchronized double getMinTransaction() {
        double minAmount = Double.POSITIVE_INFINITY;
        for (Transaction transaction : transactionsOfLastMinute) {
            double amount = transaction.getAmount();
            if (minAmount > amount)
                minAmount = amount;
        }
        return minAmount;

    }

    @Override
    public String toString() {
        return "TransactionsStatistics{" +
                "transactionsOfLastMinute=" + transactionsOfLastMinute +
                ", sum=" + sum +
                ", avg=" + avg +
                ", max=" + max +
                ", min=" + min +
                ", count=" + count +
                '}';
    }
}
