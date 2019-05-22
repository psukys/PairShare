package com.sliebald.pairshare.ui.addExpense;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;
import com.sliebald.pairshare.MainActivityViewModel;
import com.sliebald.pairshare.R;
import com.sliebald.pairshare.data.models.User;
import com.sliebald.pairshare.databinding.FragmentAddExpenseBinding;

import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

/**
 * The {@link AddExpenseFragment} gives users the option to add new expenses to the currently
 * selected list.
 */
public class AddExpenseFragment extends Fragment {

    /**
     * {@link androidx.lifecycle.ViewModel} of this fragment.
     */
    private AddExpenseViewModel mViewModel;
    private MainActivityViewModel mViewModelMain;

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
        mViewModelMain = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        setupTimePicker();
        mViewModel.getCalendar().observe(this, this::setDate);
        mBinding.button.setOnClickListener(v -> {
            if (mBinding.etAddExpense.getText().toString().isEmpty() ||
                    mBinding.etAddDate.getText().toString().isEmpty()) {
                Snackbar.make(mBinding.clAddExpenseLayout, "Expense and date cannot be empty!",
                        Snackbar.LENGTH_SHORT).show();
                return;
            }
            try {
                Double amount = Double.valueOf(mBinding.etAddExpense.getText().toString());
                mViewModelMain.getUser().observe(this, new Observer<User>() {
                    @Override
                    public void onChanged(User user) {
                        mViewModelMain.getUser().removeObserver(this);
                        mViewModel.addExpense(amount, mBinding.etAddComment.getText().toString(),
                                user.getUsername());
                        Snackbar.make(mBinding.clAddExpenseLayout,
                                "Added expense of " + mBinding.etAddExpense.getText() + " to list",
                                Snackbar.LENGTH_SHORT).show();
                        mBinding.etAddComment.getText().clear();
                        mBinding.etAddExpense.getText().clear();
                        UIUtil.hideKeyboard(Objects.requireNonNull(getActivity()));

                    }
                });

            } catch (NumberFormatException ex) {
                Snackbar.make(mBinding.clAddExpenseLayout, "Invalid date",
                        Snackbar.LENGTH_SHORT).show();
            }
        });


    }

    /**
     * Sets up the time picker when clicking on the date {@link android.widget.EditText} in the UI.
     */
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

    /**
     * Set the date in the corresponding {@link android.widget.EditText} field in the UI
     * according to the given calendar object.
     *
     * @param calendar The calendar with the date to display.
     */
    private void setDate(Calendar calendar) {
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd.MM.yyyy", Locale.getDefault());
        mBinding.etAddDate.setText(format.format(calendar.getTime()));

    }


}
