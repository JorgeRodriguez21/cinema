package com.example.cinema.service.application

import com.example.cinema.api.dto.MovieRestDto
import com.example.cinema.service.persistence.MovieService
import com.example.cinema.service.rest.MovieRestService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MovieApplicationService @Autowired constructor(
    private val movieService: MovieService,
    private val movieRestService: MovieRestService
) {
    fun getMovieDetails(id: Int): MovieRestDto {
        val movie = movieService.getMovieById(id).orElseThrow()
        val movieRestDto = movieRestService.getMovieById(movie.imdbId) ?: throw NoSuchElementException()
        movieRestDto.setComments(movie.reviews.toList())
        movieRestDto.calculateReviewRate(movie.reviews.toList())
        return movieRestDto
    }

}