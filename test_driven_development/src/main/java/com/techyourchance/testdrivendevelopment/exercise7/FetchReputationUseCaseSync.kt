package com.techyourchance.testdrivendevelopment.exercise7

import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync
import com.techyourchance.testdrivendevelopment.exercise7.networking.isSuccessful

/**
 * Created by Indri on 2019-08-21.
 */
class FetchReputationUseCaseSync(val endpoint: GetReputationHttpEndpointSync) {

    enum class UseCaseResult {
        SUCCESS,
        FAILURE,
        NETWORK_ERROR
    }

    fun fetchReputation(userId: String): UseCaseResult {
        val endpointResult = endpoint.fetchReputationSync(userId)

        if (endpointResult.isSuccessful()) {
            return UseCaseResult.SUCCESS
        }

        return UseCaseResult.FAILURE
    }


}
