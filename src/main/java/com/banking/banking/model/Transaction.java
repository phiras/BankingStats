package com.banking.banking.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * Transaction class
 * Notice the @XmlRootElement it makes spring capable of automatically turning an instant from or to a Json object
 *
 * @author Mohamad Alaloush
 */
@XmlRootElement
public class Transaction {

    private double amount;
    private Date timestamp;

    /**
     * this default constructor is essential for spring to automatically turn POJO to JSON and vice-versa
     */
    public Transaction() {

    }

    /**
     * a constructor for convenience
     *
     * @param amount
     * @param timestamp
     */
    public Transaction(double amount, Date timestamp) {
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public double getAmount() {
        return amount;
    }

    public Date getTimestamp() {
        return new Date(this.timestamp.getTime());
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "amount=" + amount +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        // you don't have to do the getclass checkit will be handeled by the casting down
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        if (Double.compare(that.amount, amount) != 0) return false;
        return timestamp != null ? timestamp.equals(that.timestamp) : that.timestamp == null;
    }

}
