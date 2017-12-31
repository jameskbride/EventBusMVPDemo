package com.jameskbride.eventbusmvpdemo.network

import java.io.Serializable

class ProfileResponse constructor(
        val firstName:String,
        val lastName:String,
        val addressLine1:String = "",
        val addressLine2:String? = "",
        val city:String = "",
        val state:String = "",
        val zipCode:String = "",
        val orderHistory:List<Order> = listOf()
): Serializable

data class Order constructor(val id:Int, val description:String = ""): Serializable