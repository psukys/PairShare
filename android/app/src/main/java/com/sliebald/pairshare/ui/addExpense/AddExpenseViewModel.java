package com.sliebald.pairshare.ui.addExpense;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sliebald.pairshare.data.Repository;
import com.sliebald.pairshare.data.models.Expense;

import java.util.Calendar;
import java.util.Objects;

/**
 * Viewmodel for the {@link AddExpenseFragment}. Handling interaction with the {@link Repository}
 * and implements other logic
 */
class AddExpenseViewModel extends ViewModel {

    /**
     * Tag for logging.
     */
    private static final String TAG = AddExpenseViewModel.class.getSimpleName();

    /**
     * Uri of the latest take image that should be added to the {@link Expense}.
     */
    private String latestImagePath;

    /**
     * image to be added to the expense.
     */
    private Bitmap image;

    /**
     * Small footprint thumbnail of the image to be added.
     */
    private Bitmap thumbnail;


    /**
     * Calender used for selecting the current time. exposed as {@link LiveData}.
     */
    private MutableLiveData<Calendar> calendar;


    /**
     * Getter for the Calender used for selecting current time.
     *
     * @return The current calender as livedata
     */
    LiveData<Calendar> getCalendar() {
        if (calendar == null) {
            calendar = new MutableLiveData<>();
            calendar.setValue(Calendar.getInstance());
        }
        return calendar;
    }

    /**
     * Updates the calendar with the given year/month/day
     *
     * @param year       Year to set.
     * @param month      Month to set.
     * @param dayOfMonth Day to set.
     */
    void setDate(int year, int month, int dayOfMonth) {
        Calendar cal = calendar.getValue();
        if (cal != null) {
            cal.set(year, month, dayOfMonth);
            calendar.setValue(cal);
        }
    }


    /**
     * Add an expense to the currently selected list.
     *
     * @param amount  Amount of money spent.
     * @param comment Comment for the expense (e.g. reason).
     * @param username Current username.
     */
    void addExpense(Double amount, String comment, String username) {
        Expense expense = new Expense();
        expense.setComment(comment);
        expense.setAmount(amount);
        expense.setUserName(username);
        expense.setTimeOfExpense(Objects.requireNonNull(calendar.getValue()).getTime());
        Repository.getInstance().addExpense(expense);
    }

    /**
     * Get the file path of the image to add to the {@link Expense}.
     *
     * @return Path. Parse with Uri.fromFile(new File(path))
     */
    public String getLatestImagePath() {
        return latestImagePath;
    }

    /**
     * Get the file path of the image to add to the {@link Expense}.
     *
     * @param latestImagePath The path to the file.
     */
    public void setLatestImagePath(String latestImagePath) {
        this.latestImagePath = latestImagePath;
    }

    /**
     * Get the image to add to the {@link Expense}.
     *
     * @return image as bitmap
     */
    public Bitmap getImage() {
        return image;
    }

    /**
     * Set the image to add to the {@link Expense}.
     *
     * @param image as Bitmap
     */
    public void setImage(Bitmap image) {
        this.image = image;
    }

    /**
     * Get the image thumbnail to add to the {@link Expense}.
     *
     * @return low size thumbnail.
     */
    public Bitmap getThumbnail() {
        return thumbnail;
    }

    /**
     * Set the image thumbnail to add to the {@link Expense}.
     *
     * @param thumbnail as Bitmap.s
     */
    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }
}
