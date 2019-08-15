package com.techyourchance.testdoublesfundamentals.example6

object Counter {

    var total: Int = 0
        private set

    fun add() {
        total++
    }

    fun add(count: Int) {
        total += count
    }
}
