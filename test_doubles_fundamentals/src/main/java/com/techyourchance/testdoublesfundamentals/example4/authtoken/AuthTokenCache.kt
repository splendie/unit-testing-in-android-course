package com.techyourchance.testdoublesfundamentals.example4.authtoken

interface AuthTokenCache {

    val authToken: String

    fun cacheAuthToken(authToken: String)
}
