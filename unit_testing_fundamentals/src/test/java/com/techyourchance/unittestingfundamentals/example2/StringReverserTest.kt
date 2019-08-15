package com.techyourchance.unittestingfundamentals.example2

import org.junit.Before
import org.junit.Test

import org.hamcrest.CoreMatchers.*
import org.junit.Assert.assertThat

class StringReverserTest {

    lateinit var SUT: StringReverser

    @Before
    @Throws(Exception::class)
    fun setup() {
        SUT = StringReverser()
    }

    @Test
    @Throws(Exception::class)
    fun reverse_emptyString_emptyStringReturned() {
        val result = SUT.reverse("")
        assertThat(result, `is`(""))
    }

    @Test
    @Throws(Exception::class)
    fun reverse_singleCharacter_sameStringReturned() {
        val result = SUT.reverse("a")
        assertThat(result, `is`("a"))
    }

    @Test
    @Throws(Exception::class)
    fun reverse_longString_reversedStringReturned() {
        val result = SUT.reverse("Vasiliy Zukanov")
        assertThat(result, `is`("vonakuZ yilisaV"))
    }
}