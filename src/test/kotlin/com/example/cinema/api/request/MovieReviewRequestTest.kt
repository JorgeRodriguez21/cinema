package com.example.cinema.api.request

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

internal class MovieReviewRequestTest {

    @Test
    fun `should throw an exception when the rate is less than 1`() {
        val actualException = assertThrows(IllegalArgumentException::class.java) { MovieReviewRequest(1, "", 0) }

        assertThat(actualException.message).isEqualTo("Rate can't be less than 1 nor more than 5")
    }

    @Test
    fun `should throw an exception when the rate is more than 5`() {
        val actualException = assertThrows(IllegalArgumentException::class.java) { MovieReviewRequest(1, "", 6) }

        assertThat(actualException.message).isEqualTo("Rate can't be less than 1 nor more than 5")
    }

    @Test
    fun `should not throw an exception when the rate is between 1 and 5`() {
        val rate = (1..5).random()
        assertDoesNotThrow { MovieReviewRequest(1, "", rate) }
    }
}