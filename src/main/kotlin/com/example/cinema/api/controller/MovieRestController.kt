package com.example.cinema.api.controller

import com.example.cinema.api.dto.MovieRestDto
import com.example.cinema.service.MovieRestService
import com.example.cinema.service.MovieService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.query.Param
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/movie")
class MovieRestController @Autowired constructor(private val service: MovieRestService) {

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getMoviesInformation(@PathVariable id: Int): MovieRestDto {
        return service.getMovieDetails(id)
    }
}
