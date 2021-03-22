package com.example.shop.api

import android.util.Log

class ApiError(message: String, cause: Throwable) : Throwable(message, cause) {

    companion object {

         const val PRECONDITION_FAILED = "HTTP 412"
         const val FORBIDDEN = "HTTP 403"
         const val NOT_FOUND = "HTTP 404"
         const val UNAUTHORIZED = "HTTP 401"
         const val INSUFFICIENT_STORAGE = "HTTP 507"
         const val NOT_VERIFIED = "HTTP 422"
         const val ACCEPTED = "HTTP 202"

        /**
         * To determine an error message based on the returned code
         */
        fun getErrorMessage(error: String): String {
            Log.d("The error is " ,error)
            var message = "Error occurred"
            when (error.trim()) {
                FORBIDDEN -> message = "You're not an authorized admin"
                NOT_FOUND -> message = "Email is not registered yet, please register!"
                UNAUTHORIZED -> message = "Email/password/verification are not correct"
                INSUFFICIENT_STORAGE -> message = "The database is full!!"
                PRECONDITION_FAILED -> message = "Is already in use"
                NOT_VERIFIED -> message = "Please verify your email"
                ACCEPTED -> message = "We've send you an email with code"
            }
            return message
        }
    }

}