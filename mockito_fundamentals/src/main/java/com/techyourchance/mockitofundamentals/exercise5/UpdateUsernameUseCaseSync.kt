package com.techyourchance.mockitofundamentals.exercise5

import com.techyourchance.mockitofundamentals.exercise5.eventbus.EventBusPoster
import com.techyourchance.mockitofundamentals.exercise5.eventbus.UserDetailsChangedEvent
import com.techyourchance.mockitofundamentals.exercise5.networking.NetworkErrorException
import com.techyourchance.mockitofundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync
import com.techyourchance.mockitofundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync.EndpointResult
import com.techyourchance.mockitofundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync.EndpointResultStatus
import com.techyourchance.mockitofundamentals.exercise5.users.User
import com.techyourchance.mockitofundamentals.exercise5.users.UsersCache

class UpdateUsernameUseCaseSync(private val updateUsernameHttpEndpointSync: UpdateUsernameHttpEndpointSync,
                                private val usersCache: UsersCache,
                                private val eventBusPoster: EventBusPoster) {

    enum class UseCaseResult {
        SUCCESS,
        FAILURE,
        NETWORK_ERROR
    }

    fun updateUsernameSync(userId: String, username: String): UseCaseResult {
        var endpointResult: EndpointResult = try {
            updateUsernameHttpEndpointSync.updateUsername(userId, username)
        } catch (e: NetworkErrorException) {
            return UseCaseResult.NETWORK_ERROR
        }

        return if (isSuccessfulEndpointResult(endpointResult)) {
            // the bug here is reversed arguments
            val user = User(endpointResult.getUsername(), endpointResult.userId)
            eventBusPoster.postEvent(UserDetailsChangedEvent(User(userId, username)))
            usersCache.cacheUser(user)

            UseCaseResult.SUCCESS
        } else {
            UseCaseResult.FAILURE
        }
    }

    private fun isSuccessfulEndpointResult(endpointResult: EndpointResult): Boolean {
        // the bug here is the wrong definition of successful response
        return endpointResult.status === EndpointResultStatus.SUCCESS || endpointResult.status === EndpointResultStatus.GENERAL_ERROR
    }
}
