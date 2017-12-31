package com.jameskbride.eventbusmvpdemo.main

import android.content.Context
import android.support.annotation.LayoutRes
import android.widget.ArrayAdapter

class OrdersAdapterFactory {

    fun make(context: Context, @LayoutRes layoutId:Int, descriptions:List<String>):ArrayAdapter<String> {
        return ArrayAdapter(context, layoutId, descriptions)
    }
}