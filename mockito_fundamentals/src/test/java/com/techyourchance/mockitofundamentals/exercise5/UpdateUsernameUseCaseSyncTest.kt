package com.techyourchance.mockitofundamentals.exercise5

import com.nhaarman.mockitokotlin2.argumentCaptor
import com.techyourchance.mockitofundamentals.exercise5.eventbus.EventBusPoster
import com.techyourchance.mockitofundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync
import com.techyourchance.mockitofundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync.*
import com.techyourchance.mockitofundamentals.exercise5.users.UsersCache
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*

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
    fun updateUsernameSync_success_userCached() {
        success()
        val ac = argumentCaptor<String>()
        SUT.updateUsernameSync(USER_ID, USER_NAME)
        verify(updateUsernameHttpEndpointSyncMock).updateUsername(ac.capture(), ac.capture())
        val values = ac.allValues
        assertThat(values[0], `is`(USER_ID))
        assertThat(values[1], `is`(USER_NAME))
    }

    fun updateUsernameSync_authError_userNotCached() {

    }

    fun updateUsernameSync_generalError_userNotCached() {

    }
    // updateUsernameSync_serverError_userNotCached
    // updateUsernameSync_success_eventBusPosted
    // updateUsernameSync_authError_eventBusNotPosted
    // updateUsernameSync_generalError_eventBusNotPosted
    // updateUsernameSync_serverError_eventBusNotPosted

    private fun success() {
        `when`(updateUsernameHttpEndpointSyncMock.updateUsername(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(EndpointResult(EndpointResultStatus.SUCCESS, USER_ID, USER_NAME))
    }

    private fun authError() {
        `when`(updateUsernameHttpEndpointSyncMock.updateUsername(any(String::class.java), any(String::class.java)))
                .thenReturn(EndpointResult(EndpointResultStatus.AUTH_ERROR))
    }

    private fun serverError() {
        `when`(updateUsernameHttpEndpointSyncMock.updateUsername(any(String::class.java), any(String::class.java)))
                .thenReturn(EndpointResult(EndpointResultStatus.SERVER_ERROR))
    }

    private fun generalError() {
        `when`(updateUsernameHttpEndpointSyncMock.updateUsername(any(String::class.java), any(String::class.java)))
                .thenReturn(EndpointResult(EndpointResultStatus.GENERAL_ERROR))

    }

    companion object {
        const val USER_NAME = "uname"
        const val USER_ID = "uid"
    }

}