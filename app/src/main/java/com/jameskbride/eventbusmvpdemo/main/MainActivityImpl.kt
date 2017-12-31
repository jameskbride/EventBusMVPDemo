package com.jameskbride.eventbusmvpdemo.main

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.jameskbride.eventbusmvpdemo.R
import com.jameskbride.eventbusmvpdemo.network.BurritosToGoApi
import com.jameskbride.eventbusmvpdemo.network.Order
import com.jameskbride.eventbusmvpdemo.network.ProfileResponse
import com.jameskbride.eventbusmvpdemo.utils.ToasterWrapper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class MainActivityImpl @Inject constructor(val burritosToGoApi: BurritosToGoApi, val toasterWrapper: ToasterWrapper = ToasterWrapper()) {

    fun onCreate(savedInstanceState: Bundle?, mainActivity: MainActivity) {
        mainActivity.setContentView(R.layout.activity_main)
    }

    fun onResume(mainActivity: MainActivity) {
        val call:Call<ProfileResponse>  = burritosToGoApi.getProfile("1")

        call.enqueue(object: Callback<ProfileResponse> {
            override fun onFailure(call: Call<ProfileResponse>?, t: Throwable?) {
                toasterWrapper.makeText(mainActivity, R.string.oops, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<ProfileResponse>?, response: Response<ProfileResponse>?) {
                setProfileDetails(mainActivity, response)

                showOrders(response?.body()?.orderHistory, mainActivity)
            }
        })
    }

    private fun setProfileDetails(mainActivity: MainActivity, response: Response<ProfileResponse>?) {
        mainActivity.findViewById<TextView>(R.id.customer_name).text = "${response?.body()?.firstName} ${response?.body()?.lastName}"
        mainActivity.findViewById<TextView>(R.id.address_line_1).text = response?.body()?.addressLine1
        mainActivity.findViewById<TextView>(R.id.address_line_2).text = response?.body()?.addressLine2
        mainActivity.findViewById<TextView>(R.id.city).text = response?.body()?.city
        mainActivity.findViewById<TextView>(R.id.state).text = response?.body()?.state
        mainActivity.findViewById<TextView>(R.id.zipcode).text = response?.body()?.zipCode
    }

    private fun showOrders(orderHistory:List<Order>?, mainActivity: MainActivity) {
        if (orderHistory?.isNotEmpty()!!) {
            mainActivity.findViewById<LinearLayout>(R.id.no_orders_block).visibility = View.VISIBLE
            mainActivity.findViewById<LinearLayout>(R.id.found_orders_block).visibility = View.GONE
        } else {
            mainActivity.findViewById<LinearLayout>(R.id.no_orders_block).visibility = View.GONE
            mainActivity.findViewById<LinearLayout>(R.id.found_orders_block).visibility = View.VISIBLE
        }
    }
}