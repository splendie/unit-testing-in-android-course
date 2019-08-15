package com.techyourchance.unittestingfundamentals.exercise3

import com.techyourchance.unittestingfundamentals.example3.Interval
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class IntervalsAdjacencyDetectorTest {

    lateinit var SUT: IntervalsAdjacencyDetector

    @Before
    fun setup() {
        SUT = IntervalsAdjacencyDetector()
    }

    @Test
    fun isAdjacent_adjacentNonOverlapping_returnsTrue() {
        val ivl1 = Interval(start = 0, end = 10)
        val ivl2 = Interval(start = 10, end = 20)
        val result = SUT.isAdjacent(ivl1, ivl2)
        Assert.assertThat(result, `is`<Boolean>(true))
    }

    @Test
    fun isAdjacent_nonAdjacent_returnsFalse() {
        val ivl1 = Interval(start = 0, end = 10)
        val ivl2 = Interval(start = 12, end = 22)
        val result = SUT.isAdjacent(ivl1, ivl2)
        Assert.assertThat(result, `is`<Boolean>(false))
    }

    @Test
    fun isAdjacent_overlapping_returnsFalse() {
        val ivl1 = Interval(start = 0, end = 10)
        val ivl2 = Interval(start = 9, end = 19)
        val result = SUT.isAdjacent(ivl1, ivl2)
        Assert.assertThat(result, `is`<Boolean>(false))
    }

    @Test
    fun isAdjacent_sameIntervals_returnsFalse() {
        val ivl1 = Interval(start = 0, end = 10)
        val ivl2 = Interval(start = 0, end = 10)
        val result = SUT.isAdjacent(ivl1, ivl2)
        Assert.assertThat(result, `is`<Boolean>(false))
    }

}