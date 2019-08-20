package com.techyourchance.testdrivendevelopment.exercise6

import com.techyourchance.testdrivendevelopment.exercise6.users.User

class FetchUserUseCaseSync {

    enum class Status {
        SUCCESS,
        FAILURE,
        NETWORK_ERROR
    }

    data class UseCaseResult(val status: Status, val user: User?)

    fun fetchUserSync(userId: String): UseCaseResult {
        return UseCaseResult(Status.FAILURE, null)
    }

}
