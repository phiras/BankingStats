package com.banking.banking.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * with @XmoRootElement annotation so spring can transform POJO to Json and back
 */

@XmlRootElement
public class Statistics {
    private double sum;
    private double avg;
    private double max;
    private double min;
    private long count;

    /**
     * this default constructor is essential for spring to automatically turn POJO to JSON and vice-versa
     */
    public Statistics() {
    }

    /**
     * convenience constructor for copying an object
     *
     * @param statistics
     */
    public Statistics(Statistics statistics) {
        this.sum = statistics.getSum();
        this.avg = statistics.getAvg();
        this.max = statistics.getMax();
        this.min = statistics.getMin();
        this.count = statistics.getCount();
    }

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

    public void setSum(double sum) {
        this.sum = sum;
    }

    public void setAvg(double avg) {
        this.avg = avg;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public void setCount(long count) {
        this.count = count;
    }

    /**
     * resets all the fields to zero
     */
    public synchronized void clear() {
        this.sum = 0;
        this.avg = 0;
        this.max = 0;
        this.min = 0;
        this.count = 0;
    }
}
