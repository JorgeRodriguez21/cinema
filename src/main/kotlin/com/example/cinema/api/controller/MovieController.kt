package com.example.cinema.api.controller

import com.example.cinema.api.request.MovieShowRequest
import com.example.cinema.service.MovieService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/movie")
class MovieController @Autowired constructor(private val service: MovieService) {

    @PostMapping("/show")
    @ResponseStatus(HttpStatus.CREATED)
    fun createShowMovieTime(@RequestBody request: MovieShowRequest) {
        val auth = SecurityContextHolder.getContext().authentication;
        if (auth != null && auth.authorities.any { it.authority.equals("ADMIN") }) {
            service.saveShowTimes(request)
        } else {
            throw ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "Invalid user"
            )
        }
    }
}