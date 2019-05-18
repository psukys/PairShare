package com.sliebald.pairshare.data.models;

/**
 * POJO for holding the summary of all {@link Expense}s of a user as part of a {@link ExpenseList}.
 */
public class ExpenseSummary {

    /**
     * Number of {@link Expense}s that the {@link User} in the current {@link ExpenseList}
     */
    private int numExpenses;

    /**
     * Number of {@link Expense}s that the {@link User} in the current {@link ExpenseList}
     */
    private double sumExpenses;

    /**
     * Default constructor for firebase.
     */
    public ExpenseSummary() {
        numExpenses = 0;
        sumExpenses = 0;
    }

    /**
     * Get the number of {@link Expense}s of the corresponding {@link User} in the current list.
     *
     * @return Number of {@link Expense}s
     */
    public int getNumExpenses() {
        return numExpenses;
    }

    /**
     * Set the number of {@link Expense}s of the corresponding {@link User} in the current list.
     *
     * @param numExpenses Number of {@link Expense}s
     */
    public void setNumExpenses(int numExpenses) {
        this.numExpenses = numExpenses;
    }

    /**
     * Get the sum of {@link Expense}s of the corresponding {@link User} in the current list.
     *
     * @return Sum of {@link Expense}s
     */
    public double getSumExpenses() {
        return sumExpenses;
    }

    /**
     * Set the sum of {@link Expense}s of the corresponding {@link User} in the current list.
     *
     * @param sumExpenses Sum {@link Expense}s
     */
    public void setSumExpenses(double sumExpenses) {
        this.sumExpenses = sumExpenses;
    }
}
