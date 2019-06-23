package com.jameskbride.eventbusmvpdemo.utils

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

class ToasterWrapper {

    private lateinit var toast: Toast

    fun makeText(context: Context, @StringRes message: Int, duration: Int):ToasterWrapper {
        this.toast = Toast.makeText(context, message, duration)

        return this
    }

    fun show() {
        toast.show()
    }
}