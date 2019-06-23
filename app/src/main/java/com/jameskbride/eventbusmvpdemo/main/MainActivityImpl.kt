package com.jameskbride.eventbusmvpdemo.main

import android.os.Bundle
import android.view.View
import android.widget.*
import com.jameskbride.eventbusmvpdemo.R
import com.jameskbride.eventbusmvpdemo.network.ProfileApi
import com.jameskbride.eventbusmvpdemo.network.Order
import com.jameskbride.eventbusmvpdemo.network.ProfileResponse
import com.jameskbride.eventbusmvpdemo.utils.ToasterWrapper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class MainActivityImpl @Inject constructor(
        val profileApi: ProfileApi,
        val toasterWrapper: ToasterWrapper = ToasterWrapper(),
        val ordersAdapterFactory:OrdersAdapterFactory = OrdersAdapterFactory()) {

    fun onCreate(savedInstanceState: Bundle?, mainActivity: MainActivity) {
        mainActivity.setContentView(R.layout.activity_main)

        mainActivity.findViewById<Button>(R.id.submit).setOnClickListener { view: View? ->
            val profileIdEdit = mainActivity.findViewById<EditText>(R.id.profile_id_edit)
            getProfile(mainActivity, profileIdEdit.text.toString())
        }
    }

    fun onResume(mainActivity: MainActivity) {
    }

    private fun getProfile(mainActivity: MainActivity, profileId: String) {
        val call: Call<ProfileResponse> = profileApi.getProfile(profileId)

        call.enqueue(object : Callback<ProfileResponse> {
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
            mainActivity.findViewById<LinearLayout>(R.id.found_orders_block).visibility = View.VISIBLE
            mainActivity.findViewById<LinearLayout>(R.id.no_orders_block).visibility = View.GONE
            val adapter = ordersAdapterFactory.make(mainActivity, android.R.layout.simple_list_item_1, orderHistory.map { it.description })
            mainActivity.findViewById<ListView>(R.id.order_list).adapter = adapter
        } else {
            mainActivity.findViewById<LinearLayout>(R.id.no_orders_block).visibility = View.VISIBLE
            mainActivity.findViewById<LinearLayout>(R.id.found_orders_block).visibility = View.GONE
        }
    }
}