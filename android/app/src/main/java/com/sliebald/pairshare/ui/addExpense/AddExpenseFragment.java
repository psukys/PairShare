package com.sliebald.pairshare.ui.addExpense;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

/**
 * The {@link AddExpenseFragment} gives users the option to add new expenses to the currently
 * selected list.
 */
public class AddExpenseFragment extends Fragment {

    private static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 342;
    /**
     * {@link androidx.lifecycle.ViewModel} of this fragment.
     */
    private AddExpenseViewModel mViewModel;
    private MainActivityViewModel mViewModelMain;

    /**
     * Request code for the external image capture activity
     */
    private static final int REQUEST_TAKE_PICTURE = 6433;


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

        // Add onClicklistener for adding an expense.
        mBinding.button.setOnClickListener(v -> {
            // check if input is valid
            if (mBinding.etAddExpense.getText().toString().isEmpty() ||
                    mBinding.etAddDate.getText().toString().isEmpty()) {
                Snackbar.make(mBinding.clAddExpenseLayout, "Expense and date cannot be empty!",
                        Snackbar.LENGTH_SHORT).show();
                return;
            }
            // Get the Username and add expense.
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

        mBinding.ibAddImage.setOnClickListener(v -> takePicture());

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


    /**
     * Starts an {@link Intent} for retrieving an image.
     */
    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (getActivity() != null && getContext() != null
                && takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {


            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            } else {


                File photoFile;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    return;
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(getContext(),
                            "com.sliebald.pairshare.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PICTURE);
                }
            }
        }

    }


    /**
     * Create a file where a full res image can be stored.
     * Based on https://developer.android.com/training/camera/photobasics#java
     *
     * @return The {@link File} for the image.
     * @throws IOException thrown in case of an IO error.
     */
    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.GERMAN).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir =
                Objects.requireNonNull(getContext()).getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        mViewModel.setLatestImagePath(image.getAbsolutePath());
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PICTURE && resultCode == RESULT_OK && data != null
                && getContext() != null) {
            try {
                Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),
                        Uri.fromFile(new File(mViewModel.getLatestImagePath())));
                mViewModel.setImage(imageBitmap);

                // optional: retrieve a thumbnail
                Bundle extras = data.getExtras();
                if (extras == null) return;
                Bitmap tumbnailBitmap = (Bitmap) extras.get("data");
                mViewModel.setThumbnail(tumbnailBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else super.onActivityResult(requestCode, resultCode, data);
    }


}
