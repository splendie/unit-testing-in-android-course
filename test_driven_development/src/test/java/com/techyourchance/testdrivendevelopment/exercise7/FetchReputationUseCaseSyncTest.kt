package com.techyourchance.testdrivendevelopment.exercise7

import com.nhaarman.mockitokotlin2.argumentCaptor
import com.techyourchance.testdrivendevelopment.exercise7.FetchReputationUseCaseSync.*
import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync
import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync.*
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

        assertThat(result, `is`(UseCaseResult.SUCCESS))
    }

    @Test
    fun fetchReputation_networkError_failureReturned() {
        generalError()
        val userId = USER_ID

        val result = SUT.fetchReputation(userId)

        assertThat(result, `is`(UseCaseResult.FAILURE))
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
                .thenReturn(EndpointResult(EndpointStatus.NETWORK_ERROR, GetReputationHttpEndpointSync.UNKNOWN_REPUTATION))
    }

    //endregion helper methods


    //region helper classes
    //endregion helper classes


    companion object {
        const val USER_ID = "userId"
    }

}