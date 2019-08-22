package com.techyourchance.testdrivendevelopment.exercise7

import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doThrow
import com.techyourchance.testdrivendevelopment.exercise7.FetchReputationUseCaseSync.*
import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync
import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync.*
import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync.Companion.GOOD_REPUTATION
import com.techyourchance.testdrivendevelopment.exercise7.networking.NetworkErrorException
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Indri on 2019-08-21.
 */

@RunWith(MockitoJUnitRunner::class)
internal class FetchReputationUseCaseSyncTest {

    lateinit var SUT: FetchReputationUseCaseSync

    @Mock
    lateinit var endpointMock: GetReputationHttpEndpointSync


    @Before
    fun setup() {
        SUT = FetchReputationUseCaseSync(endpointMock)
    }

    @Test
    fun fetchReputation_success_userIdPassedToEndpoint() {
        success()
        val ac = argumentCaptor<String>()
        val userId = USER_ID

        SUT.fetchReputation(userId)
        verify(endpointMock).fetchReputationSync(ac.capture())

        assertThat(ac.firstValue, `is`(userId))
    }

    @Test
    fun fetchReputation_success_successReturned() {
        success()
        val userId = USER_ID

        val result = SUT.fetchReputation(userId)

        assertThat(result.status, `is`(UseCaseResultStatus.SUCCESS))
    }

    @Test
    fun fetchReputation_generalError_failureReturned() {
        generalError()
        val userId = USER_ID

        val result = SUT.fetchReputation(userId)

        assertThat(result.status, `is`(UseCaseResultStatus.FAILURE))
    }

    @Test
    fun fetchReputation_networkError_networkErrorReturned() {
        networkError()
        val userId = USER_ID

        val result = SUT.fetchReputation(userId)

        assertThat(result.status, `is`(UseCaseResultStatus.NETWORK_ERROR))
    }

    @Test
    fun fetchReputation_success_reputationReturned() {
        success()
        val userId = USER_ID

        val result = SUT.fetchReputation(userId)

        assertThat(result.reputation, `is`(GOOD_REPUTATION))
    }

    @Test
    fun fetchReputation_generalError_zeroReturned() {
        generalError()
        val userId = USER_ID

        val result = SUT.fetchReputation(userId)

        assertThat(result.reputation, `is`(0))
    }

    @Test
    fun fetchReputation_networkError_zeroReturned() {
        networkError()
        val userId = USER_ID

        val result = SUT.fetchReputation(userId)

        assertThat(result.reputation, `is`(0))
    }

    //region helper methods
    private fun success() {
        `when`(endpointMock.fetchReputationSync(ArgumentMatchers.anyString()))
                .thenReturn(EndpointResult(EndpointStatus.SUCCESS, GetReputationHttpEndpointSync.GOOD_REPUTATION))
    }

    private fun generalError() {
        `when`(endpointMock.fetchReputationSync(ArgumentMatchers.anyString()))
                .thenReturn(EndpointResult(EndpointStatus.GENERAL_ERROR, GetReputationHttpEndpointSync.UNKNOWN_REPUTATION))
    }

    private fun networkError() {
        `when`(endpointMock.fetchReputationSync(ArgumentMatchers.anyString()))
                .doThrow(NetworkErrorException())
    }

    //endregion helper methods


    //region helper classes
    //endregion helper classes


    companion object {
        const val USER_ID = "userId"
    }

}