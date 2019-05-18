package com.sliebald.pairshare.ui.selectExpenseList;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.sliebald.pairshare.MyApplication;
import com.sliebald.pairshare.R;
import com.sliebald.pairshare.data.models.ExpenseList;
import com.sliebald.pairshare.utils.PreferenceUtils;

public class ExpenseListHolder extends RecyclerView.ViewHolder {


    private final TextView mName;
    private final TextView mBalance;
    private final CardView mCardView;

    private String mListID;


    public ExpenseListHolder(@NonNull View itemView) {
        super(itemView);
        mName = itemView.findViewById(R.id.label_expense_list_name);
        mCardView = itemView.findViewById(R.id.cv_expense_list_item);
        mBalance = itemView.findViewById(R.id.tv_list_balance);
    }

    public void bind(@NonNull ExpenseList expenseList, String listID) {
        mListID = listID;
        setListName(expenseList.getListName());
        setBalance(expenseList);
        mCardView.setOnClickListener(view -> {
            PreferenceUtils.setSelectedSharedExpenseListID(mListID);
        });
        PreferenceUtils.registerSelectedListChangedListener((sharedPreferences,
                                                             key) -> {
            if (key != null && key.equals(PreferenceUtils.PREFERENCE_KEY_SELECTED_EXPENSE)) {
                String changedList =
                        sharedPreferences.getString(PreferenceUtils.PREFERENCE_KEY_SELECTED_EXPENSE, "");
                if (changedList != null && !changedList.equals(mListID)) {
                    setBackgroundColor(R.color.white);
                } else {
                    setBackgroundColor(R.color.colorPrimaryLight);
                }
            }
        });
        String selectedList =
                PreferenceUtils.getSelectedSharedExpenseListID();
        if (selectedList != null && selectedList.equals(mListID)) {
            setBackgroundColor(R.color.colorPrimaryLight);
        }

    }

    private void setBackgroundColor(int color) {
        mCardView.setCardBackgroundColor(MyApplication.getContext()
                .getResources().getColor(color, null));
    }


    private void setListName(@Nullable String listName) {
        mName.setText(listName);
    }

    private void setBalance(ExpenseList expenseList) {
        String myId = FirebaseAuth.getInstance().getUid();
        //use int for rounding
        int amount = 0;
        for (String id : expenseList.getSharerInfo().keySet()) {
            if (id.equals(myId)) amount += expenseList.getSharerInfo().get(id).getSumExpenses();
            else amount -= expenseList.getSharerInfo().get(id).getSumExpenses();
        }
        if (amount >= 100)
            mBalance.setTextColor(MyApplication.getContext()
                    .getResources().getColor(R.color.balance_positive, null));
        else if (amount > 0)
            mBalance.setTextColor(MyApplication.getContext()
                    .getResources().getColor(R.color.balance_slight_positive, null));

        else if (amount < 0 && amount > -100)
            mBalance.setTextColor(MyApplication.getContext()
                    .getResources().getColor(R.color.balance_slight_negative, null));
        else if (amount <= -100)
            mBalance.setTextColor(MyApplication.getContext()
                    .getResources().getColor(R.color.balance_negative, null));

        mBalance.setText(String.format("%d", amount));

    }

}
