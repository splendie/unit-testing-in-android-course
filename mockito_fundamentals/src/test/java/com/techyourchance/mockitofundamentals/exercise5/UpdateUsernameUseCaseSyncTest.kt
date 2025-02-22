package com.techyourchance.mockitofundamentals.exercise5

import com.nhaarman.mockitokotlin2.argumentCaptor
import com.techyourchance.mockitofundamentals.exercise5.eventbus.EventBusPoster
import com.techyourchance.mockitofundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync
import com.techyourchance.mockitofundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync.*
import com.techyourchance.mockitofundamentals.exercise5.users.User
import com.techyourchance.mockitofundamentals.exercise5.users.UsersCache
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UpdateUsernameUseCaseSyncTest {

    lateinit var SUT: UpdateUsernameUseCaseSync

    lateinit var updateUsernameHttpEndpointSyncMock: UpdateUsernameHttpEndpointSync
    lateinit var usersCacheMock: UsersCache
    lateinit var eventBusPosterMock: EventBusPoster

    @Before
    fun setup() {
        updateUsernameHttpEndpointSyncMock = mock(UpdateUsernameHttpEndpointSync::class.java)
        usersCacheMock = mock(UsersCache::class.java)
        eventBusPosterMock = mock(EventBusPoster::class.java)

        SUT = UpdateUsernameUseCaseSync(updateUsernameHttpEndpointSyncMock, usersCacheMock, eventBusPosterMock)
        success()
    }

    @Test
    fun updateUsernameSync_success_useridAndUsernamePassedToEndpoint() {
        // Arrange
        success()
        val ac = argumentCaptor<String>()
        // Action
        SUT.updateUsernameSync(USER_ID, USER_NAME)
        // Assert
        verify(updateUsernameHttpEndpointSyncMock).updateUsername(ac.capture(), ac.capture())
        val values = ac.allValues
        assertThat(values[0], `is`(USER_ID))
        assertThat(values[1], `is`(USER_NAME))
    }

    @Test
    fun updateUsernameSync_success_userCached() {
        // Arrange
        success()
        val ac = argumentCaptor<User>()
        // Action
        SUT.updateUsernameSync(userId = USER_ID, username = USER_NAME)
        // Assert
        verify(usersCacheMock).cacheUser(ac.capture())
        val cachedUser = ac.firstValue
        assertThat(cachedUser.userId, `is`(USER_ID))
        assertThat(cachedUser.username, `is`(USER_NAME))
    }

    @Test
    fun updateUsernameSync_authError_userNotCached() {
        // Arrange
        authError()
        // Action
        SUT.updateUsernameSync(userId = USER_ID, username = USER_NAME)
        // Assert
        verifyNoMoreInteractions(usersCacheMock)
    }

    //TODO
//    @Test
//    fun updateUsernameSync_generalError_userNotCached() {
//
//    }

    //TODO
    // updateUsernameSync_serverError_userNotCached

    //TODO
    // updateUsernameSync_success_eventBusPosted

    //TODO
    // updateUsernameSync_authError_eventBusNotPosted

    //TODO
    // updateUsernameSync_generalError_eventBusNotPosted

    //TODO
    // updateUsernameSync_serverError_eventBusNotPosted

    private fun success() {
        `when`(updateUsernameHttpEndpointSyncMock.updateUsername(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(EndpointResult(EndpointResultStatus.SUCCESS, USER_ID, USER_NAME))
    }

    private fun authError() {
        `when`(updateUsernameHttpEndpointSyncMock.updateUsername(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(EndpointResult(EndpointResultStatus.AUTH_ERROR))
    }

    private fun serverError() {
        `when`(updateUsernameHttpEndpointSyncMock.updateUsername(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(EndpointResult(EndpointResultStatus.SERVER_ERROR))
    }

    private fun generalError() {
        `when`(updateUsernameHttpEndpointSyncMock.updateUsername(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(EndpointResult(EndpointResultStatus.GENERAL_ERROR))
    }

    companion object {
        const val USER_NAME = "uname"
        const val USER_ID = "uid"
    }

}