package com.techyourchance.mockitofundamentals.exercise5.users

interface UsersCache {

    fun cacheUser(user: User)

    fun getUser(userId: String): User

}
