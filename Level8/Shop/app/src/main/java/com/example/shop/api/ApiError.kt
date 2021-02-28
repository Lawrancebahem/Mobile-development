package com.example.shop.api

import android.util.Log

class ApiError(message: String, cause: Throwable) : Throwable(message, cause) {

    companion object {

        private const val PRECONDITION_FAILED = "HTTP 412"
        private const val FORBIDDEN = "HTTP 403"
        private const val NOT_FOUND = "HTTP 404"
        private const val UNAUTHORIZED = "HTTP 401"
        private const val INSUFFICIENT_STORAGE = "HTTP 507"

        /**
         * To determine an error message based on the returned code
         */
        fun getErrorMessage(error: String): String {
            Log.d("The error is " ,error)
            var message = "Error occurred"
            when (error.trim()) {

                FORBIDDEN -> message = "You're not an authorized admin"
                NOT_FOUND -> message = "Email is not registered yet, please register!"
                UNAUTHORIZED -> message = "Email/password are not correct"
                INSUFFICIENT_STORAGE -> message = "The database is full!!"
                PRECONDITION_FAILED -> message = "Is already in use"
            }
            return message
        }
    }

}