package com.techyourchance.testdrivendevelopment.exercise7.networking


interface GetReputationHttpEndpointSync {

    val reputationSync: EndpointResult

    enum class EndpointStatus {
        SUCCESS,
        GENERAL_ERROR,
        NETWORK_ERROR
    }

    class EndpointResult(val status: EndpointStatus, val reputation: Int)

    fun fetchReputationSync(userId: String): EndpointResult

    companion object {
        const val UNKNOWN_REPUTATION = 0
        const val GOOD_REPUTATION = 1
        const val BAD_REPUTATION = 2

    }
}

fun GetReputationHttpEndpointSync.EndpointResult.isSuccessful(): Boolean = this.status == GetReputationHttpEndpointSync.EndpointStatus.SUCCESS

