package com.jameskbride.eventbusmvpdemo.main

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.jameskbride.eventbusmvpdemo.R
import com.jameskbride.eventbusmvpdemo.network.Order
import com.jameskbride.eventbusmvpdemo.network.ProfileResponse
import com.jameskbride.eventbusmvpdemo.utils.ToasterWrapper
import javax.inject.Inject

class MainActivityImpl @Inject constructor(
        val presenter: MainActivityPresenter,
        val toasterWrapper: ToasterWrapper = ToasterWrapper(),
        val ordersAdapterFactory:OrdersAdapterFactory = OrdersAdapterFactory()
): MainActivityView {
    private lateinit var mainActivity: MainActivity

    fun onCreate(savedInstanceState: Bundle?, mainActivity: MainActivity) {
        this.mainActivity = mainActivity
        mainActivity.setContentView(R.layout.activity_main)
        presenter.view = this
    }

    fun onResume() {
        presenter.open()
        presenter.getProfile("1")
    }

    override fun displayError(message: Int) {
        toasterWrapper.makeText(mainActivity, message, Toast.LENGTH_LONG).show()
    }

    override fun displayProfileDetails(profileResponse: ProfileResponse) {
        setProfileDetails(mainActivity, profileResponse!!)
    }

    override fun displayOrders(orderHistory: List<Order>) {
        mainActivity.findViewById<LinearLayout>(R.id.found_orders_block).visibility = View.VISIBLE
        mainActivity.findViewById<LinearLayout>(R.id.no_orders_block).visibility = View.GONE
        val adapter = ordersAdapterFactory.make(mainActivity, android.R.layout.simple_list_item_1, orderHistory.map { it.description })
        mainActivity.findViewById<ListView>(R.id.order_list).adapter = adapter
    }

    override fun displayNoOrders() {
        mainActivity.findViewById<LinearLayout>(R.id.no_orders_block).visibility = View.VISIBLE
        mainActivity.findViewById<LinearLayout>(R.id.found_orders_block).visibility = View.GONE
    }

    private fun setProfileDetails(mainActivity: MainActivity, response: ProfileResponse?) {
        mainActivity.findViewById<TextView>(R.id.customer_name).text = "${response?.firstName} ${response?.lastName}"
        mainActivity.findViewById<TextView>(R.id.address_line_1).text = response?.addressLine1
        mainActivity.findViewById<TextView>(R.id.address_line_2).text = response?.addressLine2
        mainActivity.findViewById<TextView>(R.id.city).text = response?.city
        mainActivity.findViewById<TextView>(R.id.state).text = response?.state
        mainActivity.findViewById<TextView>(R.id.zipcode).text = response?.zipCode
    }

    fun onPause() {
        presenter.close()
    }
}