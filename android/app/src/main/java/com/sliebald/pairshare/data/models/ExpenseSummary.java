package com.sliebald.pairshare.data.models;

public class ExpenseSummary {

    private int numExpenses;
    private double sumExpenses;
    private double avgExpenses;

    /**
     * Default constructor for firebase.
     */
    public ExpenseSummary() {

    }

    public ExpenseSummary(int numExpenses, double sumExpenses, double avgExpenses) {
        this.numExpenses = numExpenses;
        this.sumExpenses = sumExpenses;
        this.avgExpenses = avgExpenses;
    }

    public int getNumExpenses() {
        return numExpenses;
    }

    public void setNumExpenses(int numExpenses) {
        this.numExpenses = numExpenses;
    }

    public double getSumExpenses() {
        return sumExpenses;
    }

    public void setSumExpenses(double sumExpenses) {
        this.sumExpenses = sumExpenses;
    }

    public double getAvgExpenses() {
        return avgExpenses;
    }

    public void setAvgExpenses(double avgExpenses) {
        this.avgExpenses = avgExpenses;
    }
}
