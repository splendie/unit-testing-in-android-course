package com.techyourchance.testdrivendevelopment.exercise7

import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync
import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync.Companion.GOOD_REPUTATION
import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync.Companion.UNKNOWN_REPUTATION
import com.techyourchance.testdrivendevelopment.exercise7.networking.NetworkErrorException
import com.techyourchance.testdrivendevelopment.exercise7.networking.isSuccessful

/**
 * Created by Indri on 2019-08-21.
 */
class FetchReputationUseCaseSync(val endpoint: GetReputationHttpEndpointSync) {

    enum class UseCaseResultStatus {
        SUCCESS,
        FAILURE,
        NETWORK_ERROR
    }

    data class UseCaseResult(val status: UseCaseResultStatus, val reputation: Int = UNKNOWN_REPUTATION)

    fun fetchReputation(userId: String): UseCaseResult {
        try {
            val endpointResult = endpoint.fetchReputationSync(userId)

            if (endpointResult.isSuccessful()) {
                return UseCaseResult(UseCaseResultStatus.SUCCESS, GOOD_REPUTATION)
            }
        } catch (e: NetworkErrorException) {
            return UseCaseResult(UseCaseResultStatus.NETWORK_ERROR)
        }


        return UseCaseResult(UseCaseResultStatus.FAILURE)
    }


}
