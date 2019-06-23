package com.jameskbride.eventbusmvpdemo.main

import android.content.Context
import android.widget.ArrayAdapter
import androidx.annotation.LayoutRes

class OrdersAdapterFactory {

    fun make(context: Context, @LayoutRes layoutId:Int, descriptions:List<String>):ArrayAdapter<String> {
        return ArrayAdapter(context, layoutId, descriptions)
    }
}