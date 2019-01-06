package com.sliebald.pairshare.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.sliebald.pairshare.R;
import com.sliebald.pairshare.databinding.FragmentAddEntryBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

public class AddEntryFragment extends Fragment {

    /**
     * Databinding of the corresponding fragment layout.
     */
    private FragmentAddEntryBinding mBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout
                .fragment_add_entry, container, false);

        setupTimePicker();
        return mBinding.getRoot();
    }

    private void setupTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        mBinding.etAddDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull
                        (getContext()), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int
                            dayOfMonth) {
                        setDate(year, month, dayOfMonth);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get
                        (Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get
                (Calendar.DAY_OF_MONTH));
    }

    private void setDate(int year, int month, int day) {
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd.MM.yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        mBinding.etAddDate.setText(format.format(calendar.getTime()));

    }
}