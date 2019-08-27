package com.techyourchance.unittestinginandroid.example12

import android.content.Context
import com.nhaarman.mockitokotlin2.whenever

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.invocation.InvocationOnMock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.stubbing.Answer

import java.util.ArrayList
import java.util.Collections

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.Mockito.`when`

@RunWith(MockitoJUnitRunner::class)
class StringRetrieverTest {
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    internal var mContextMock: Context? = null
    // endregion helper fields ---------------------------------------------------------------------

    lateinit var SUT: StringRetriever

    @Before
    @Throws(Exception::class)
    fun setup() {
        SUT = StringRetriever(mContextMock)
    }

    @Test
    @Throws(Exception::class)
    fun getString_correctParameterPassedToContext() {
        // Arrange
        // Act
        SUT.getString(ID)
        // Assert
        verify<Context>(mContextMock).getString(ID)
    }

    @Test
    @Throws(Exception::class)
    fun getString_correctResultReturned() {
        // Arrange
        whenever(mContextMock!!.getString(ID)).thenReturn(STRING)
        // Act
        val result = SUT.getString(ID)
        // Assert
        assertThat(result, `is`(STRING))
    }

    companion object {

        // region constants ----------------------------------------------------------------------------
        val ID = 10
        val STRING = "string"
    }

    // region helper methods -----------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------

}