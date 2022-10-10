package com.studioviolet.wordle.api

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ResultAdapterFactory @Inject constructor() : CallAdapter.Factory() {

    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        val rawReturnType: Class<*> = getRawType(returnType)
        if (rawReturnType == Call::class.java) {
            if (returnType is ParameterizedType) {
                val callInnerType: Type = getParameterUpperBound(0, returnType)
                if (getRawType(callInnerType) == Result::class.java) {
                    return if (callInnerType is ParameterizedType) {
                        val resultInnerType = getParameterUpperBound(0, callInnerType)
                        TryCallAdapter<Any?>(resultInnerType)
                    } else {
                        TryCallAdapter<Nothing>(Nothing::class.java)
                    }
                }
            }
        }

        return null
    }
}

private class TryCallAdapter<R>(
    private val type: Type
) : CallAdapter<R, Call<Result<R>>> {

    override fun responseType() = type

    override fun adapt(call: Call<R>): Call<Result<R>> = ResultCall(call)
}
