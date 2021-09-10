package com.example.cinema.persistence.repository

import com.example.cinema.persistence.model.Movie
import org.springframework.data.repository.CrudRepository

interface MovieRepository : CrudRepository<Movie, Int> {
    fun findByImdbId(imdbId: String): Movie?
}