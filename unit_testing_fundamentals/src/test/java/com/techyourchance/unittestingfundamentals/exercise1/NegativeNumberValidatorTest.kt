package com.techyourchance.unittestingfundamentals.exercise1

import org.hamcrest.core.Is.`is`
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * Created by Indri on 2019-08-14.
 */
class NegativeNumberValidatorTest {

    lateinit var SUT: NegativeNumberValidator

    @Before
    fun setup() {
        SUT = NegativeNumberValidator()
    }

    @Test
    fun isNegative_givenNegative_returnsTrue() {
        val result = SUT.isNegative(-3)
        Assert.assertThat(result, `is`<Boolean>(true))
    }

    @Test
    fun isNegative_givenPositive_returnsFalse() {
        val result = SUT.isNegative(2)
        Assert.assertThat(result, `is`<Boolean>(false))
    }

    @Test
    fun isNegative_givenZero_returnsFalse() {
        val result = SUT.isNegative(0)
        Assert.assertThat(result, `is`<Boolean>(false))
    }
}