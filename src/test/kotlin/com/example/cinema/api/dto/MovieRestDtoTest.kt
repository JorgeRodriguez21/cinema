package com.example.cinema.api.dto

import com.example.cinema.persistence.model.Review
import com.example.cinema.persistence.model.Role
import com.example.cinema.persistence.model.User
import com.example.cinema.utils.TestMovieBuilder.buildMovie
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class MovieRestDtoTest {

    @Test
    fun `should calculate review average rate from movie reviews`() {
        val user = User(1, "", "", Role.ADMIN)
        val movie = buildMovie()
        val review = Review(1, 5, "none", user, movie)
        val review2 = Review(1, 3, "none", user, movie)
        val review3 = Review(1, 1, "none", user, movie)
        val review4 = Review(1, 2, "none", user, movie)
        val reviews = listOf(review, review2, review3, review4)
        val movieRestDto = MovieRestDto("", "", "", "", 0.0, "", listOf())

        movieRestDto.calculateReviewRate(reviews)

        assertThat(movieRestDto.getReviewsOnCinema()).isEqualTo(2.75)
    }

    @Test
    fun `should return all the comments in reviews as one list`() {
        val user = User(1, "", "", Role.ADMIN)
        val movie = buildMovie()
        val review = Review(1, 5, "none", user, movie)
        val review2 = Review(1, 3, "none1", user, movie)
        val review3 = Review(1, 1, "none2", user, movie)
        val review4 = Review(1, 2, "none3", user, movie)
        val reviews = listOf(review, review2, review3, review4)
        val expectedComments = listOf("none", "none1", "none2", "none3")
        val movieRestDto = MovieRestDto("", "", "", "", 0.0, "", listOf())

        movieRestDto.setComments(reviews)

        assertThat(movieRestDto.getCommentsOnCinema()).usingRecursiveComparison().isEqualTo(expectedComments)
    }

}