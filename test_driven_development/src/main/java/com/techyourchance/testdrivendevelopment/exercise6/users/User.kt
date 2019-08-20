package com.techyourchance.testdrivendevelopment.exercise6.users

data class User(val userId: String, val username: String) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val user = other as User
        return if (userId != user.userId) false else username == user.username
    }

    override fun hashCode(): Int {
        var result = userId.hashCode()
        result = 31 * result + username.hashCode()
        return result
    }
}
