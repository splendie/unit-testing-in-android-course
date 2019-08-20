package com.techyourchance.testdrivendevelopment.exercise6

import com.nhaarman.mockitokotlin2.argumentCaptor
import com.techyourchance.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpointSync
import com.techyourchance.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpointSync.*
import com.techyourchance.testdrivendevelopment.exercise6.networking.NetworkErrorException
import com.techyourchance.testdrivendevelopment.exercise6.users.User
import com.techyourchance.testdrivendevelopment.exercise6.users.UsersCache
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner




/**
 * Created by Indri on 2019-08-20.
 */
@RunWith(MockitoJUnitRunner::class)
internal class FetchUserUseCaseSyncTest {

    lateinit var SUT: FetchUserUseCaseSync

    @Mock
    lateinit var endpointMock: FetchUserHttpEndpointSync

    @Mock
    lateinit var cacheMock: UsersCache

    @Before
    fun setup() {
        SUT = FetchUserUseCaseSync(endpointMock, cacheMock)
    }

    // 1. if user is not in cacheMock, remote is called
    // 2. if user is in cacheMock, remote is not called
    // 3. if user is in cacheMock, user from cacheMock is returned

    // 4. if fetch remote succeed, userId is passed to endpoint
    // 5. if fetch remote succeed, return user from remote

    // 6. if fetch remote succeed, success is returned
    // 7. if fetch remote general error, failure is returned
    // if fetch remote network error, network error is returned

    // if fetch remote succeed, user is cached
    // if fetch remote failure, user is not cached
    // if fetch remote network error, user is not cached



    @Test
    fun fetchUserSync_userNotInCache_remoteFetchIsCalled() {
        // Arrange
        nullifyUserInCache()
        success()
        val userId = USER_ID

        // Act
        SUT.fetchUserSync(userId)

        // Assert
        verify(endpointMock).fetchUserSync(userId)
    }

    @Test
    fun fetchUserSync_userInCache_remoteFetchIsNotCalled() {
        // Arrange
        provideUserInCache()
        val userId = USER_ID

        // Act
        SUT.fetchUserSync(userId)

        // Assert
        verify(endpointMock, times(0)).fetchUserSync(userId)
    }

    @Test
    fun fetchUserSync_userInCache_userFromCacheReturned() {
        // Arrange
        provideUserInCache()
        val userId = USER_ID

        // Act
        val result = SUT.fetchUserSync(userId)

        // Assert
        assertNotNull(result.user)
        val cachedUser = cacheMock.getUser(userId)
        assertNotNull(cachedUser)
        val cachedUserId = cachedUser!!.userId
        assertThat(result.user!!.userId, `is`(cachedUserId))
    }

    @Test
    fun fetchUserSync_remoteFetchSuccess_userIdIsPassedToEndpoint() {
        // Arrange
        nullifyUserInCache()
        success()
        val userId = USER_ID
        val ac = argumentCaptor<String>()

        // Act
        SUT.fetchUserSync(userId)

        // Assert
        verify(endpointMock).fetchUserSync(ac.capture())
        assertThat(ac.firstValue, `is`(USER_ID))
    }

    @Test
    fun fetchUserSync_remoteFetchSuccess_userIsReturned() {
        nullifyUserInCache()
        success()
        val userId = USER_ID

        val result = SUT.fetchUserSync(userId)

        assertNotNull(result.user)
        assertThat(result.user!!.userId, `is`(userId))
    }

    @Test
    fun fetchUserSync_remoteFetchSuccess_successIsReturned() {
        nullifyUserInCache()
        success()
        val userId = USER_ID

        val result = SUT.fetchUserSync(userId)

        assertThat(result.status, `is`(FetchUserUseCaseSync.Status.SUCCESS))
    }

    @Test
    fun fetchUserSync_remoteFetchGeneralError_failureIsReturned() {
        nullifyUserInCache()
        generalError()
        val userId = USER_ID

        val result = SUT.fetchUserSync(userId)

        assertThat(result.status, `is`(FetchUserUseCaseSync.Status.FAILURE))
    }

    @Test
    fun fetchUserSync_remoteFetchAuthError_failureIsReturned() {
        nullifyUserInCache()
        authError()
        val userId = USER_ID

        val result = SUT.fetchUserSync(userId)

        assertThat(result.status, `is`(FetchUserUseCaseSync.Status.FAILURE))
    }


    @Test
    fun fetchUserSync_remoteNetworkError_networkErrorReturned() {
        nullifyUserInCache()
        networkError()
        val userId = USER_ID

        val result = SUT.fetchUserSync(userId)

        assertThat(result.status, `is`(FetchUserUseCaseSync.Status.NETWORK_ERROR))
    }

    @Test
    fun fetchUserSync_remoteFetchSuccess_userIsCached() {
        // Arrange
        val userId = USER_ID
        val ac = argumentCaptor<User>()
        nullifyUserInCache()
        success()

        // Act
        SUT.fetchUserSync(userId)

        // Assert
        verify(cacheMock).cacheUser(ac.capture())
        assertThat(ac.firstValue.userId, `is`(USER_ID))
    }

    // It's hard to make negative test fail because the first time we implement production code
    // (to make positive test pass), it also takes care of the negative test. OR AM I DOING IT WRONG?
    // Oh, I guess, I should've not checked `isSuccessfulEndpointResult` before I wrote this negative test
    @Test
    fun fetchUserSync_remoteFetchAuthError_userIsNotCached() {
        // Arrange
        val userId = USER_ID
        nullifyUserInCache()
        authError()

        SUT.fetchUserSync(userId)

        verify(cacheMock, times(0)).cacheUser(ArgumentMatchers.any())
    }



    //region helper methods
    fun nullifyUserInCache() {
        `when`(cacheMock.getUser(ArgumentMatchers.anyString())).thenReturn(null)
    }

    fun provideUserInCache() {
        `when`(cacheMock.getUser(ArgumentMatchers.anyString())).thenAnswer {
            val userId = it.arguments[0] as String
            User(userId, USERNAME)
        }
    }

    fun success() {
        `when`(endpointMock.fetchUserSync(ArgumentMatchers.anyString()))
                .thenReturn(EndpointResult(EndpointStatus.SUCCESS, USER_ID, USERNAME))
    }

    fun authError() {
        `when`(endpointMock.fetchUserSync(ArgumentMatchers.anyString()))
                .thenReturn(EndpointResult(EndpointStatus.AUTH_ERROR))
    }

    fun generalError() {
        `when`(endpointMock.fetchUserSync(ArgumentMatchers.anyString()))
                .thenReturn(EndpointResult(EndpointStatus.GENERAL_ERROR))
    }

    fun networkError() {
        `when`(endpointMock.fetchUserSync(ArgumentMatchers.anyString()))
                .thenThrow(NetworkErrorException())
    }

    //endregion helper methods

    companion object {
        const val USER_ID = "userId"
        const val USERNAME = "username"
    }
}