package com.studioviolet.wordle.repository

import androidx.compose.ui.text.toUpperCase
import com.studioviolet.wordle.api.Result
import com.studioviolet.wordle.api.WordleApi
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

interface WordleRepository {
    suspend fun getRandomWord(): String
    suspend fun isValid(word: String): Boolean
}

@Singleton
internal class WordleRepositoryImpl @Inject constructor(
    private val api: WordleApi
) : WordleRepository {

    override suspend fun getRandomWord(): String {
        when (val result = api.getRandomWord()) {
            is Result.Success -> {
                return result.value.word.uppercase()
            }
            is Result.Failure -> {
                throw Exception("GetRandomWord failed")
            }
        }
    }

    override suspend fun isValid(word: String): Boolean {
        when (val result = api.isValid(word.lowercase())) {
            is Result.Success -> {
                return result.value.isValid
            }
            is Result.Failure -> {
                throw Exception("IsValid failed")
            }
        }
    }
}
