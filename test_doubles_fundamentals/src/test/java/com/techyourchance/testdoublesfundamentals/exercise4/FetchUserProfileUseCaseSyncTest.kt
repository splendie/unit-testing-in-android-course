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

    // fetchUserProfileSync, if network error then network error is returned

    // fetchUserProfileSync, if success then user object is cached
    // fetchUserProfileSync, if fail  then object is not cached

    @Test
    fun fetch_correctUserId_successReturned() {
        val status = SUT.fetchUserProfileSync(CORRECT_USER_ID)
        Assert.assertThat(status, `is`<FetchUserProfileUseCaseSync.UseCaseResult>(FetchUserProfileUseCaseSync.UseCaseResult.SUCCESS))
    }

    @Test
    fun fetch_emptyUserId_failureReturned() {
        endpointSyncTd.isAuthError = true
        val status = SUT.fetchUserProfileSync("")
        Assert.assertThat(status, `is`<FetchUserProfileUseCaseSync.UseCaseResult>(FetchUserProfileUseCaseSync.UseCaseResult.FAILURE))
    }

    @Test
    fun fetch_correctUserId_userIdIsCached() {
        val userId = CORRECT_USER_ID
        SUT.fetchUserProfileSync(userId)
        val result = cacheTd.getUser(userId)
        Assert.assertNotNull(result)
    }

    @Test
    fun fetch_emptyUserId_userIdIsNotCached() {
        endpointSyncTd.isAuthError = true
        val userId = ""
        SUT.fetchUserProfileSync(userId)
        val result = cacheTd.getUser(userId)
        Assert.assertNull(result)
    }

    companion object {
        const val CORRECT_USER_ID = "correct_user_id"
    }

//    ----------------------------------------------------------------------------------------------
//    Helper classes

    class UserProfileHttpEndpointSyncTd : UserProfileHttpEndpointSync {
        var isAuthError: Boolean = false
        var isServerError: Boolean = false
        var isGeneralError: Boolean = false

        override fun getUserProfile(userId: String): UserProfileHttpEndpointSync.EndpointResult {
            return if (isAuthError) {
                UserProfileHttpEndpointSync.EndpointResult(status = EndpointResultStatus.AUTH_ERROR)
            } else if (isServerError) {
                UserProfileHttpEndpointSync.EndpointResult(status = EndpointResultStatus.SERVER_ERROR)
            } else if (isGeneralError) {
                UserProfileHttpEndpointSync.EndpointResult(status = EndpointResultStatus.GENERAL_ERROR)
            } else {
                UserProfileHttpEndpointSync.EndpointResult(status = EndpointResultStatus.SUCCESS, userId = userId, fullName = FULLNAME, imageUrl = IMAGE_URL)
            }
        }

        companion object {
            const val FULLNAME = "Full Name"
            const val IMAGE_URL = "someImageUrl"
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