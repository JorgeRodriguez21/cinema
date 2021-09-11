package com.example.cinema.api.controller

import com.example.cinema.api.request.MovieShowDeleteRequest
import com.example.cinema.api.request.MovieShowRequest
import com.example.cinema.api.response.MovieResponse
import com.example.cinema.service.MovieService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/movie")
class MovieController @Autowired constructor(private val service: MovieService) {

    @PostMapping("/show")
    @ResponseStatus(HttpStatus.CREATED)
    fun createShowMovieTime(@RequestBody request: MovieShowRequest) {
        if (isAdmin()) {
            service.saveShowTimes(request)
        } else {
            throw ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "Invalid user"
            )
        }
    }

    @GetMapping("/show")
    @ResponseStatus(HttpStatus.OK)
    fun getMoviesInformation(): MovieResponse {
        if (isAdmin()) {
            return MovieResponse(service.retrieveAllMoviesInformation())
        } else {
            throw ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "Invalid user"
            )
        }
    }

    @DeleteMapping("/show")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteMovieShow(@RequestBody request: MovieShowDeleteRequest) {
        if (isAdmin()) {
            service.deleteMovieShow(request)
        } else {
            throw ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "Invalid user"
            )
        }
    }

    private fun isAdmin(): Boolean {
        val auth = SecurityContextHolder.getContext().authentication
        return auth != null && auth.authorities.any { it.authority.equals("ADMIN") }
    }
}
