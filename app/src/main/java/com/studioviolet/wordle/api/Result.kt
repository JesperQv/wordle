package com.studioviolet.wordle.api

sealed class Result<out T> {

    class Success<T>(val value: T) : Result<T>()

    class Failure(
        val message: String,
        val error: Throwable
    ) : Result<Nothing>()

    fun successValueOrNull(): T? {
        if (this is Success) {
            return this.value
        }
        return null
    }

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success [value=$value]"
            is Failure -> "Failure [error=$error]"
        }
    }
}
