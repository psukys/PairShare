package com.sliebald.pairshare.ui.selectExpenseList;

import android.view.View;
import android.widget.TextView;

import com.sliebald.pairshare.MyApplication;
import com.sliebald.pairshare.R;
import com.sliebald.pairshare.data.models.ExpenseList;
import com.sliebald.pairshare.utils.PreferenceUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ExpenseListHolder extends RecyclerView.ViewHolder {


    private final TextView mName;
    private final CardView mCardView;
    private String mListID;


    public ExpenseListHolder(@NonNull View itemView) {
        super(itemView);
        mName = itemView.findViewById(R.id.label_expense_list_name);
        mCardView = itemView.findViewById(R.id.cv_expense_list_item);
    }

    public void bind(@NonNull ExpenseList expenseList, String listID) {
        mListID = listID;
        setListName(expenseList.getListName());
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
}
