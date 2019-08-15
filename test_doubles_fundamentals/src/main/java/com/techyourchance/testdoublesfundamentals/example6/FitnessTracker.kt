package com.techyourchance.testdoublesfundamentals.example6

class FitnessTracker {

    val totalSteps: Int
        get() = Counter.total

    fun step() {
        Counter.add()
    }

    fun runStep() {
        Counter.add(RUN_STEPS_FACTOR)
    }

    companion object {

        const val RUN_STEPS_FACTOR = 2
    }
}
