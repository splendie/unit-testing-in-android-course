package com.techyourchance.mockitofundamentals.example8

class UserMess(val fullName: String, val address: Address, val phoneNumber: PhoneNumber) {

    // real implementation here
    val connectedUsers: List<UserMess>?
        get() = null

    fun logOut() {
        // real implementation here
    }

    fun connectWith(otherUser: UserMess) {
        // real implementation here
    }

    fun disconnectFromAll() {
        // real implementation here
    }
}
