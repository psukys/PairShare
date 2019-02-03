package com.sliebald.pairshare.ui.selectExpenseList;

import android.view.View;
import android.widget.TextView;

import com.sliebald.pairshare.R;
import com.sliebald.pairshare.data.models.ExpenseList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class InviteListHolder extends RecyclerView.ViewHolder {


    private final TextView mName;
    private final CardView mCardView;
    private String mListID;


    public InviteListHolder(@NonNull View itemView) {
        super(itemView);
        mName = itemView.findViewById(R.id.label_expense_list_name);
        mCardView = itemView.findViewById(R.id.cv_expense_list_item);
    }

    public void bind(@NonNull ExpenseList expenseList, String listID) {
        mListID = listID;
        setListName(expenseList.getListName());

    }

    private void setListName(@Nullable String listName) {
        mName.setText(listName);
    }
}
