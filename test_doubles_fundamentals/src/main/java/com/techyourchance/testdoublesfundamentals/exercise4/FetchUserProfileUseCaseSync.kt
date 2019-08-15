package com.techyourchance.testdoublesfundamentals.exercise4

import com.techyourchance.testdoublesfundamentals.example4.networking.NetworkErrorException
import com.techyourchance.testdoublesfundamentals.exercise4.networking.UserProfileHttpEndpointSync
import com.techyourchance.testdoublesfundamentals.exercise4.networking.UserProfileHttpEndpointSync.EndpointResult
import com.techyourchance.testdoublesfundamentals.exercise4.users.User
import com.techyourchance.testdoublesfundamentals.exercise4.users.UsersCache

class FetchUserProfileUseCaseSync(private val mUserProfileHttpEndpointSync: UserProfileHttpEndpointSync,
                                  private val mUsersCache: UsersCache) {

    enum class UseCaseResult {
        SUCCESS,
        FAILURE,
        NETWORK_ERROR
    }

    fun fetchUserProfileSync(userId: String): UseCaseResult {
        val endpointResult: EndpointResult
        try {
            endpointResult = mUserProfileHttpEndpointSync.getUserProfile(userId)
        } catch (e: NetworkErrorException) {
            return UseCaseResult.NETWORK_ERROR
        }

        if (isSuccessfulEndpointResult(endpointResult)) {
            mUsersCache.cacheUser(User(userId, endpointResult.fullName, endpointResult.imageUrl))
            return UseCaseResult.SUCCESS
        }

        return UseCaseResult.FAILURE
    }

    private fun isSuccessfulEndpointResult(endpointResult: EndpointResult): Boolean {
        return endpointResult.status === UserProfileHttpEndpointSync.EndpointResultStatus.SUCCESS
    }
}
