package com.example.cinema.service.persistence

import com.example.cinema.api.request.MovieReviewRequest
import com.example.cinema.persistence.model.Review
import com.example.cinema.persistence.model.Role
import com.example.cinema.persistence.model.User
import com.example.cinema.persistence.repository.MovieRepository
import com.example.cinema.persistence.repository.ReviewRepository
import com.example.cinema.persistence.repository.UserRepository
import com.example.cinema.utils.TestMovieBuilder.buildMovie
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.NoSuchElementException

internal class ReviewServiceTest {

    private val movieRepository: MovieRepository = mockk()
    private val userRepository: UserRepository = mockk()
    private val reviewRepository: ReviewRepository = mockk()

    @Test
    fun `should call the repository to find the movie by id`() {
        val movieRequest = MovieReviewRequest(1, "comment", 2, "username")
        every { movieRepository.findById(movieRequest.movieId) } returns (Optional.empty())
        val reviewService = ReviewService(movieRepository, userRepository, reviewRepository)
        val user = User(1, "", "", Role.USER)
        val movie = buildMovie()
        every { movieRepository.findById(movieRequest.movieId) } returns (Optional.of(movie))
        every { userRepository.findByUsername(any()) } returns user
        val review = Review(null, movieRequest.rate, movieRequest.comment, user, movie)
        every { reviewRepository.save(any()) }.returns(review)

        reviewService.addReviewForAMovie(movieRequest)

        verify(exactly = 1) { movieRepository.findById(movieRequest.movieId) }
    }

    @Test
    fun `should throw when movie is not found`() {
        val movieRequest = MovieReviewRequest(1, "comment", 2, "username")
        every { movieRepository.findById(movieRequest.movieId) } returns (Optional.empty())
        val reviewService = ReviewService(movieRepository, userRepository, reviewRepository)

        assertThrows(NoSuchElementException::class.java) { reviewService.addReviewForAMovie(movieRequest) }
    }

    @Test
    fun `should call repository to find a user by username`() {
        val movieRequest = MovieReviewRequest(1, "comment", 2, "username")
        val user = User(1, "", "", Role.USER)
        val movie = buildMovie()
        every { movieRepository.findById(any()) } returns (Optional.of(movie))
        every { userRepository.findByUsername(any()) } returns user
        val review = Review(null, movieRequest.rate, movieRequest.comment, user, movie)
        every { reviewRepository.save(any()) }.returns(review)
        val reviewService = ReviewService(movieRepository, userRepository, reviewRepository)

        reviewService.addReviewForAMovie(movieRequest)

        verify(exactly = 1) { userRepository.findByUsername("username") }
    }

    @Test
    fun `should throw when user is not found`() {
        val movieRequest = MovieReviewRequest(1, "comment", 2, "username")
        val movie = buildMovie()
        every { movieRepository.findById(any()) } returns (Optional.of(movie))
        every { userRepository.findByUsername(any()) } returns null
        val reviewService = ReviewService(movieRepository, userRepository, reviewRepository)

        val actualException =
            assertThrows(NoSuchElementException::class.java) { reviewService.addReviewForAMovie(movieRequest) }

        assertThat(actualException.message).isEqualTo("Invalid user")
    }

    @Test
    fun `should call repository to save a review`() {
        val movieRequest = MovieReviewRequest(1, "comment", 2, "username")
        val user = User(1, "", "", Role.USER)
        val movie = buildMovie()
        every { movieRepository.findById(movieRequest.movieId) } returns (Optional.of(movie))
        every { userRepository.findByUsername(any()) } returns user
        val review = Review(null, movieRequest.rate, movieRequest.comment, user, movie)
        every { reviewRepository.save(any()) }.returns(review)
        val reviewService = ReviewService(movieRepository, userRepository, reviewRepository)

        reviewService.addReviewForAMovie(movieRequest)

        verify(exactly = 1) { reviewRepository.save(review) }
    }
}