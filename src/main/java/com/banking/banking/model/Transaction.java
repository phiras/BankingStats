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
    // this is not a json format, if you are aiming to .. replace '=' with ':'.
    public String toString() {
        return "Transaction{" +
                "amount=" + amount +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        // I think you don't have to compare the class, this will behandled by the casting operation down
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;
        if (Double.compare(that.amount, amount) != 0) return false;
        return timestamp != null ? timestamp.equals(that.timestamp) : that.timestamp == null;
    }

}
