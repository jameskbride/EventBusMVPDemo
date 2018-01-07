package com.jameskbride.eventbusmvpdemo.utils;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

public class ToasterWrapper {

    private Toast toast;

    public ToasterWrapper makeText(Context context, @StringRes int message, int duration) {
        this.toast = Toast.makeText(context, message, duration);

        return this;
    }

    public void show() {
        toast.show();
    }
}