package com.studioviolet.wordle.api

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

internal interface WordleApi {

    @GET("/get/")
    suspend fun getRandomWord(): Result<WordResponse>

    @GET("/ask/")
    suspend fun isValid(@Query("word") word: String): Result<ValidWordResponse>
}

data class WordResponse(
    @SerializedName("Response") val word: String
)

data class ValidWordResponse(
    @SerializedName("Response") val isValid: Boolean
)
