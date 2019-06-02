package com.sliebald.pairshare.utils;

import android.graphics.Color;

import com.sliebald.pairshare.MyApplication;
import com.sliebald.pairshare.R;
import com.sliebald.pairshare.data.models.ExpenseList;
import com.sliebald.pairshare.data.models.ExpenseSummary;

import java.util.Map;

/**
 * Utilityclass for managing {@link com.sliebald.pairshare.data.models.ExpenseList}.
 */
public class ExpenseListUtils {

    /**
     * Returns the total difference of the sum of all expenses of the given userId minus the sum
     * of expenses of other users.
     *
     * @param userId      User to count positively
     * @param expenseList Expenselist to check.
     * @return Difference as double. Positive values mean the given userId paid more, negative
     * values mean he paid less than the other user(s) in that list.
     */
    public static double getExpenseDifferenceFor(String userId, ExpenseList expenseList) {
        double difference = 0;

        for (Map.Entry<String, ExpenseSummary> entry : expenseList.getSharerInfo().entrySet()) {
            if (entry.getKey().equals(userId))
                difference += entry.getValue().getSumExpenses();
            else
                difference -= entry.getValue().getSumExpenses();
        }
        return difference;
    }

    /**
     * Return the {@link Color} matching to the difference in spending between the current and
     * other users. Positive values --> Green, Negative --> Red.
     *
     * @param difference Difference in spending between current and other users.
     * @return Color based on the difference.
     */
    public static int getExpenseDifferenceColor(double difference) {

        if (difference >= 100)
            return MyApplication.getContext()
                    .getResources().getColor(R.color.balance_positive, null);
        else if (difference > 0)
            return MyApplication.getContext()
                    .getResources().getColor(R.color.balance_slight_positive, null);

        else if (difference < 0 && difference > -100)
            return MyApplication.getContext()
                    .getResources().getColor(R.color.balance_slight_negative, null);
        else if (difference <= -100)
            return MyApplication.getContext()
                    .getResources().getColor(R.color.balance_negative, null);
        else return Color.BLACK;
    }

}
