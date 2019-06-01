package com.sliebald.pairshare.utils;

import android.graphics.Bitmap;

public class ImageUtils {
    public static Bitmap getResizedBitmap(Bitmap img, int maxSize) {
        int width = img.getWidth();
        int height = img.getHeight();

        double ratio = (double) width / (double) height;
        if (ratio > 1) {
            width = maxSize;
            height = (int) (width / ratio);
        } else {
            height = maxSize;
            width = (int) (height * ratio);
        }
        return Bitmap.createScaledBitmap(img,
                width,
                height,
                true);
    }
}
