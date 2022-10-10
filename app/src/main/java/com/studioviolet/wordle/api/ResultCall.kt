package com.studioviolet.wordle.api

import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

internal class ResultCall<T>(
    proxy: Call<T>
) : CallDelegate<T, Result<T>>(proxy) {

    override fun enqueueImpl(callback: Callback<Result<T>>) {
        proxy.enqueue(TryCallback(this, callback))
    }

    override fun cloneImpl(): ResultCall<T> {
        return ResultCall(proxy.clone())
    }

    private class TryCallback<T>(
        private val proxy: ResultCall<T>,
        private val callback: Callback<Result<T>>
    ) : Callback<T> {

        @Suppress("UNCHECKED_CAST")
        override fun onResponse(call: Call<T>, response: Response<T>) {
            val result: Result<T> = if (response.isSuccessful) {
                Result.Success(value = response.body() as T)
            } else {
                val bodyString = response.errorBody()?.string() ?: ""
                val code = response.code()
                val exception = BackendException(message = bodyString,
                    httpErrorCode = code)

                Result.Failure("Network Error", exception)
            }
            callback.onResponse(proxy, Response.success(result))
        }

        override fun onFailure(call: Call<T>, error: Throwable) {
            val result = Result.Failure("Network error", error)
            callback.onResponse(proxy, Response.success(result))
        }
    }

    override fun timeout(): Timeout = proxy.timeout()

    companion object {
        /**
         * 400 - client error
         * 401 - auth error
         * 403 - no access
         * 409 - conflict error
         * 429 - too many requests
         */
        private val ERROR_HTTP_CODES = setOf(400, 401, 403, 409, 429)
    }
}

class BackendException(
    message: String,
    val httpErrorCode: Int,
    cause: Throwable? = null
) : RuntimeException(message, cause)

internal abstract class CallDelegate<In, Out>(protected val proxy: Call<In>) : Call<Out> {

    override fun execute(): Response<Out> = throw NotImplementedError()

    final override fun enqueue(callback: Callback<Out>) = enqueueImpl(callback)

    final override fun clone(): Call<Out> = cloneImpl()

    override fun cancel() = proxy.cancel()

    override fun request(): Request = proxy.request()

    override fun isExecuted() = proxy.isExecuted

    override fun isCanceled() = proxy.isCanceled

    abstract fun enqueueImpl(callback: Callback<Out>)

    abstract fun cloneImpl(): Call<Out>
}
