package com.sliebald.pairshare.ui.addExpense;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sliebald.pairshare.R;
import com.sliebald.pairshare.databinding.FragmentAddExpenseBinding;

import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class AddExpenseFragment extends Fragment {

    private AddExpenseViewModel mViewModel;

    /**
     * Databinding of the corresponding fragment layout.
     */
    private FragmentAddExpenseBinding mBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout
                .fragment_add_expense, container, false);

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AddExpenseViewModel.class);
    }

    @Override
    public void onStart() {
        super.onStart();
        mViewModel.getCalendar().observe(this, this::setDate);
        mViewModel.updateUser();
        setupTimePicker();
    }


    private void setupTimePicker() {
        mBinding.etAddDate.setOnClickListener(v -> {
            UIUtil.hideKeyboard(Objects.requireNonNull(getActivity()));

            DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull
                    (getContext()), (view, year, month, dayOfMonth) -> {
                mViewModel.setDate(year, month, dayOfMonth);

            }, Objects.requireNonNull(mViewModel.getCalendar().getValue()).get(Calendar.YEAR),
                    mViewModel.getCalendar().getValue().get(Calendar.MONTH),
                    mViewModel.getCalendar().getValue().get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });
    }

    private void setDate(Calendar calendar) {
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd.MM.yyyy", Locale.getDefault());
        mBinding.etAddDate.setText(format.format(calendar.getTime()));

    }


}
