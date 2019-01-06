package com.sliebald.pairshare.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.sliebald.pairshare.R;
import com.sliebald.pairshare.databinding.FragmentAddExpenseBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

public class AddExpenseFragment extends Fragment {

    /**
     * Databinding of the corresponding fragment layout.
     */
    private FragmentAddExpenseBinding mBinding;

    private Calendar mSelectedDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout
                .fragment_add_expense, container, false);
        setupTimePicker();
        return mBinding.getRoot();
    }

    private void setupTimePicker() {
        mSelectedDate = Calendar.getInstance();
        mBinding.etAddDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull
                        (getContext()), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int
                            dayOfMonth) {
                        mSelectedDate.set(year, month, dayOfMonth);
                        setDate();
                    }
                }, mSelectedDate.get(Calendar.YEAR), mSelectedDate.get(Calendar.MONTH),
                        mSelectedDate.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        setDate();
    }

    private void setDate() {
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd.MM.yyyy", Locale.getDefault());
        mBinding.etAddDate.setText(format.format(mSelectedDate.getTime()));

    }
}