package com.example.cinema.api.controller

import com.example.cinema.api.request.MovieShowRequest
import com.example.cinema.service.MovieService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/movie")
class MovieController @Autowired constructor(private val service: MovieService) {

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/show")
    @ResponseStatus(HttpStatus.CREATED)
    fun createShowMovieTime(@RequestBody request: MovieShowRequest) {
        service.saveShowTimes(request)
    }
}