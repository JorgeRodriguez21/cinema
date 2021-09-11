package com.example.cinema.api.controller

import com.example.cinema.api.request.MovieReviewRequest
import com.example.cinema.service.persistence.ReviewService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.CurrentSecurityContext
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/review")
class ReviewController @Autowired constructor(private val service: ReviewService) {

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    fun createShowMovieTime(
        @CurrentSecurityContext(expression = "authentication?.name") username: String,
        @RequestBody request: MovieReviewRequest
    ) {
        request.username = username
        service.addReviewForAMovie(request)
    }
}
