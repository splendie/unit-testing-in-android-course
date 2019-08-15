package com.techyourchance.testdoublesfundamentals.exercise4

import com.techyourchance.testdoublesfundamentals.exercise4.networking.UserProfileHttpEndpointSync
import com.techyourchance.testdoublesfundamentals.exercise4.users.User
import com.techyourchance.testdoublesfundamentals.exercise4.users.UsersCache
import org.junit.Before
import com.techyourchance.testdoublesfundamentals.exercise4.networking.UserProfileHttpEndpointSync.EndpointResultStatus
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert
import org.junit.Test

class FetchUserProfileUseCaseSyncTest {

    lateinit var SUT: FetchUserProfileUseCaseSync
    lateinit var endpointSyncTd: UserProfileHttpEndpointSyncTd
    lateinit var cacheTd: UsersCacheTd

    @Before
    fun setup() {
        endpointSyncTd = UserProfileHttpEndpointSyncTd()
        cacheTd = UsersCacheTd()

        SUT = FetchUserProfileUseCaseSync(endpointSyncTd, cacheTd)
    }

    @Test
    fun fetchUserProfileSync_success_successReturned() {
        val status = SUT.fetchUserProfileSync(USER_ID)
        Assert.assertThat(status, `is`<FetchUserProfileUseCaseSync.UseCaseResult>(FetchUserProfileUseCaseSync.UseCaseResult.SUCCESS))
    }

    @Test
    fun fetchUserProfileSync_emptyUserId_failureReturned() {
        val status = SUT.fetchUserProfileSync("")
        Assert.assertThat(status, `is`<FetchUserProfileUseCaseSync.UseCaseResult>(FetchUserProfileUseCaseSync.UseCaseResult.FAILURE))
    }

    @Test
    fun fetchUserProfileSync_authError_failureReturned() {
        endpointSyncTd.isAuthError = true
        val status = SUT.fetchUserProfileSync("")
        Assert.assertThat(status, `is`<FetchUserProfileUseCaseSync.UseCaseResult>(FetchUserProfileUseCaseSync.UseCaseResult.FAILURE))
    }

    @Test
    fun fetchUserProfileSync_serverError_failureReturned() {
        endpointSyncTd.isServerError = true
        val status = SUT.fetchUserProfileSync("")
        Assert.assertThat(status, `is`<FetchUserProfileUseCaseSync.UseCaseResult>(FetchUserProfileUseCaseSync.UseCaseResult.FAILURE))
    }

    @Test
    fun fetchUserProfileSync_generalError_failureReturned() {
        endpointSyncTd.isGeneralError = true
        val status = SUT.fetchUserProfileSync("")
        Assert.assertThat(status, `is`<FetchUserProfileUseCaseSync.UseCaseResult>(FetchUserProfileUseCaseSync.UseCaseResult.FAILURE))
    }

    @Test
    fun fetchUserProfileSync_success_userCached() {
        val userId = USER_ID
        SUT.fetchUserProfileSync(userId)
        val result = cacheTd.getUser(userId)
        Assert.assertNotNull(result)
    }

    @Test
    fun fetchUserProfileSync_emptyUserId_userNotCached() {
        val userId = ""
        SUT.fetchUserProfileSync(userId)
        val result = cacheTd.getUser(userId)
        Assert.assertNull(result)
    }

    @Test
    fun fetchUserProfileSync_authError_userNotCached() {
        endpointSyncTd.isAuthError = true
        val userId = USER_ID
        SUT.fetchUserProfileSync(userId)
        val result = cacheTd.getUser(userId)
        Assert.assertNull(result)
    }

    @Test
    fun fetchUserProfileSync_generalError_userNotCached() {
        endpointSyncTd.isGeneralError = true
        val userId = USER_ID
        SUT.fetchUserProfileSync(userId)
        val result = cacheTd.getUser(userId)
        Assert.assertNull(result)
    }

    @Test
    fun fetchUserProfileSync_serverError_userNotCached() {
        endpointSyncTd.isServerError = true
        val userId = USER_ID
        SUT.fetchUserProfileSync(userId)
        val result = cacheTd.getUser(userId)
        Assert.assertNull(result)
    }

    @Test
    fun fetchUserProfileSync_success_userIdPassedToEndpoint() {
        SUT.fetchUserProfileSync(USER_ID)
        val userId = endpointSyncTd.userId
        Assert.assertThat(USER_ID, `is`<String>(userId))
    }

    companion object {
        const val USER_ID = "userId"
        const val FULL_NAME = "Full Name"
        const val IMAGE_URL = "someImageUrl"
    }

//    ----------------------------------------------------------------------------------------------
//    Helper classes

    class UserProfileHttpEndpointSyncTd : UserProfileHttpEndpointSync {
        var isAuthError: Boolean = false
        var isServerError: Boolean = false
        var isGeneralError: Boolean = false

        var userId: String = ""

        override fun getUserProfile(userId: String): UserProfileHttpEndpointSync.EndpointResult {
            this.userId = userId

            return if (isAuthError) {
                UserProfileHttpEndpointSync.EndpointResult(status = EndpointResultStatus.AUTH_ERROR)
            } else if (isServerError) {
                UserProfileHttpEndpointSync.EndpointResult(status = EndpointResultStatus.SERVER_ERROR)
            } else if (isGeneralError || userId.isEmpty()) {
                UserProfileHttpEndpointSync.EndpointResult(status = EndpointResultStatus.GENERAL_ERROR)
            } else {
                UserProfileHttpEndpointSync.EndpointResult(status = EndpointResultStatus.SUCCESS, userId = userId, fullName = FULL_NAME, imageUrl = IMAGE_URL)
            }
        }

    }

    // playing the role of "fake" test double
    class UsersCacheTd : UsersCache {
        private var usersMap: MutableMap<String, User> = hashMapOf()

        override fun cacheUser(user: User) {
            usersMap[user.userId] = user
        }

        override fun getUser(userId: String): User? = usersMap[userId]

    }
}