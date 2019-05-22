package com.sliebald.pairshare.ui.overviewExpenses;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.sliebald.pairshare.MyApplication;
import com.sliebald.pairshare.R;
import com.sliebald.pairshare.data.models.Expense;

import java.util.Locale;

/**
 * {@link androidx.recyclerview.widget.RecyclerView.ViewHolder} class representing the
 * {@link View} of a single Expense
 */
public class ExpenseHolder extends RecyclerView.ViewHolder {

    private final TextView mComment;
    //    private final TextView mExpenseDate;
    private final TextView mExpenseAmount;
    private final CardView mCardView;

    public ExpenseHolder(@NonNull View itemView) {
        super(itemView);
        mComment = itemView.findViewById(R.id.tv_label_comment);
        mExpenseAmount = itemView.findViewById(R.id.tv_label_amount);
//        mExpenseDate = itemView.findViewById(R.id.tv_list_balance);
        mCardView = itemView.findViewById(R.id.cv_expense_item);

    }

    public void bind(@NonNull Expense Expense) {


        String myId = FirebaseAuth.getInstance().getUid();

        ViewGroup.MarginLayoutParams params =
                (ViewGroup.MarginLayoutParams) mCardView.getLayoutParams();
        if (Expense.getUserID().equals(myId)) {
            mCardView.setCardBackgroundColor(MyApplication.getContext()
                    .getResources().getColor(R.color.colorPrimaryLight, null));
//            params.setMargins(40, 10, 10, 10);
            params.setMarginEnd(15);
            params.setMarginStart(80);

        } else {
            mCardView.setCardBackgroundColor(MyApplication.getContext()
                    .getResources().getColor(R.color.balance_negative, null));
//            params.setMargins(10, 10, 80, 10);
            params.setMarginStart(15);
            params.setMarginEnd(80);
        }
        mCardView.requestLayout();
        mComment.setText(Expense.getComment());
        mExpenseAmount.setText(String.format(Locale.GERMAN, "%.2f€", Expense.getAmount()));
    }


}
