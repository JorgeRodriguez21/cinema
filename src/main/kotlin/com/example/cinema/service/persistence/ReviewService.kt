package com.example.cinema.service.persistence

import com.example.cinema.api.request.MovieReviewRequest
import com.example.cinema.persistence.model.Review
import com.example.cinema.persistence.repository.MovieRepository
import com.example.cinema.persistence.repository.ReviewRepository
import com.example.cinema.persistence.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ReviewService @Autowired constructor(
    private val movieRepository: MovieRepository,
    private val userRepository: UserRepository,
    private val reviewRepository: ReviewRepository
) {
    fun addReviewForAMovie(movieRequest: MovieReviewRequest) {
        val movie = movieRepository.findById(movieRequest.movieId).orElseThrow()
        val user = userRepository.findByUsername(movieRequest.username) ?: throw NoSuchElementException("Invalid user")
        reviewRepository.save(Review(null, movieRequest.rate, movieRequest.comment, user, movie))
    }
}