package com.sliebald.pairshare.ui.selectExpenseList;

import android.view.View;
import android.widget.TextView;

import com.sliebald.pairshare.R;
import com.sliebald.pairshare.data.models.ExpenseList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class ExpenseListHolder extends RecyclerView.ViewHolder {


    private final TextView mName;


    public ExpenseListHolder(@NonNull View itemView) {
        super(itemView);
        mName = itemView.findViewById(R.id.label_expense_list_name);
    }

    public void bind(@NonNull ExpenseList expenseList) {
        setListName(expenseList.getListName());
    }

    private void setListName(@Nullable String listName) {
        mName.setText(listName);
    }
}
