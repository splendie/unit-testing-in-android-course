package com.techyourchance.testdrivendevelopment.exercise6

import com.techyourchance.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpointSync
import com.techyourchance.testdrivendevelopment.exercise6.networking.NetworkErrorException
import com.techyourchance.testdrivendevelopment.exercise6.users.User
import com.techyourchance.testdrivendevelopment.exercise6.users.UsersCache

class FetchUserUseCaseSync(val fetchUserHttpEndpointSync: FetchUserHttpEndpointSync,
                           val cache: UsersCache) {

    enum class Status {
        SUCCESS,
        FAILURE,
        NETWORK_ERROR
    }

    data class UseCaseResult(val status: Status, val user: User?)

    fun fetchUserSync(userId: String): UseCaseResult {
        if (cache.getUser(userId) == null) {

            try {
                val result = fetchUserHttpEndpointSync.fetchUserSync(userId)

                if (isSuccessfulEndpointResult(result)) {
                    val user = User(userId = result.userId, username = result.username)
                    cache.cacheUser(user)
                    return UseCaseResult(Status.SUCCESS, user)
                }
            } catch (e: NetworkErrorException) {
                return UseCaseResult(Status.NETWORK_ERROR, null)
            }

        } else {
            return UseCaseResult(Status.SUCCESS, cache.getUser(userId))
        }
        return UseCaseResult(Status.FAILURE, null)
    }

    private fun isSuccessfulEndpointResult(endpointResult: FetchUserHttpEndpointSync.EndpointResult): Boolean
    = endpointResult.status == FetchUserHttpEndpointSync.EndpointStatus.SUCCESS
}
