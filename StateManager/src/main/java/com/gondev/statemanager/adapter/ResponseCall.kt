package com.gondev.statemanager.adapter

import com.gondev.statemanager.state.Status
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.util.*

class ResponseCall<T> constructor(
    private val callDelegate: Call<T>,
) : Call<Status<T>> {

    override fun enqueue(callback: Callback<Status<T>>) =
        callDelegate.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                response.body()?.let {
                    when (response.code()) {
                        in 200..299 -> {
                            callback.onResponse(this@ResponseCall,
                                Response.success(Status.Success(it)))
                        }
                        else -> {
                            callback.onResponse(this@ResponseCall,
                                Response.success(Status.error(HttpException(response),
                                    response.body())))
                        }
                    }
                } ?: callback.onResponse(this@ResponseCall,
                    Response.success(Status.error(EmptyStackException(), null)))
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                callback.onResponse(this@ResponseCall, Response.success(Status.error(t, null)))
                call.cancel()
            }
        })

    override fun clone(): Call<Status<T>> = ResponseCall(callDelegate.clone())

    override fun execute(): Response<Status<T>> =
        throw UnsupportedOperationException("ResponseCall does not support execute.")

    override fun isExecuted(): Boolean = callDelegate.isExecuted

    override fun cancel() = callDelegate.cancel()

    override fun isCanceled(): Boolean = callDelegate.isCanceled

    override fun request(): Request = callDelegate.request()

    override fun timeout(): Timeout = callDelegate.timeout()
}