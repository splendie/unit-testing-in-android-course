package com.techyourchance.unittestingfundamentals.exercise2

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * Created by Indri on 2019-08-14.
 */
class StringDuplicatorTest {

    @Before
    fun setup() {
        SUT = StringDuplicator()
    }

    @Test
    fun duplicate_givenSomeString_returnsTheStringItself() {
        val result = SUT.duplicate(SOME_STRING)
        Assert.assertThat(result, `is`<String>(SOME_STRING+ SOME_STRING))
    }

    companion object {
        lateinit var SUT: StringDuplicator

        const val SOME_STRING = "Some String"
    }
}