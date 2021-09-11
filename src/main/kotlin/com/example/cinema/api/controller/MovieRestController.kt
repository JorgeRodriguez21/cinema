package com.example.cinema.api.controller

import com.example.cinema.api.dto.MovieRestDto
import com.example.cinema.service.application.MovieApplicationService
import com.example.cinema.service.rest.MovieRestService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/movie")
class MovieRestController @Autowired constructor(private val service: MovieApplicationService) {

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getMoviesInformation(@PathVariable id: Int): MovieRestDto {
        return service.getMovieDetails(id)
    }
}
