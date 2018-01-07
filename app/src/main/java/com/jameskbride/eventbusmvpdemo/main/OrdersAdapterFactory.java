package com.jameskbride.eventbusmvpdemo.main;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.widget.ArrayAdapter;

import java.util.List;

class OrdersAdapterFactory {

    public ArrayAdapter<String> make(Context context, @LayoutRes int layoutId, List<String> descriptions) {
        return new ArrayAdapter(context, layoutId, descriptions);
    }
}