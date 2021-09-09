package com.example.cinema.components

import com.example.cinema.dto.MovieRest
import com.example.cinema.service.MovieRestService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class InitialRunner @Autowired constructor(private val movieRestService: MovieRestService) : CommandLineRunner {

    val list = mutableListOf<MovieRest>()

    override fun run(vararg args: String?) {
        movieRestService.loadOrders()
    }
}