package com.jameskbride.eventbusmvpdemo

import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class BaseCallFake<T> : Call<T> {
    override fun enqueue(callback: Callback<T>?) {
        TODO("not implemented")
    }

    override fun isCanceled(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun request(): Request {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clone(): Call<T> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun cancel() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun execute(): Response<T> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isExecuted(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class SuccessCallFake<T> constructor(val response: Response<T>): BaseCallFake<T>() {
    override fun enqueue(callback: Callback<T>?) {
        callback?.onResponse(this, response)
    }
}

class FailureCallFake<T> constructor(val throwable: Throwable): BaseCallFake<T>() {
    override fun enqueue(callback: Callback<T>?) {
        callback?.onFailure(this, throwable)
    }
}