package com.techyourchance.unittestingfundamentals.exercise3

import com.techyourchance.unittestingfundamentals.example3.Interval

class IntervalsAdjacencyDetector {

    /**
     * @return true if the intervals are adjacent, but don't overlap
     */
    fun isAdjacent(interval1: Interval, interval2: Interval): Boolean =
            interval1.end == interval2.start && !isSameIntervals(interval1, interval2)

    private fun isSameIntervals(interval1: Interval, interval2: Interval): Boolean =
            interval1.start == interval2.start && interval1.end == interval2.end


}
