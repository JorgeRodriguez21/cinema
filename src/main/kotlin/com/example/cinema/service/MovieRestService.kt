package com.example.cinema.service

import com.example.cinema.api.dto.MovieRest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class MovieRestService @Autowired constructor(
    private val restTemplate: RestTemplate
) {
    companion object {
        const val API_KEY = ""
    }

    private val baseUrl = "https://www.omdbapi.com/?apikey=${API_KEY}&i="

    fun getMovieById(id: String): MovieRest? {
        return restTemplate.getForObject("$baseUrl${id}", MovieRest::class.java)
    }

    suspend fun getMovieByIdAsync(id: String): MovieRest? {
        return withContext(Dispatchers.IO) {
            restTemplate.getForObject("$baseUrl${id}", MovieRest::class.java)
        }
    }

    fun loadMovies(): List<MovieRest> {
        val obtainedMovies = mutableListOf<MovieRest>()
        val movieIds = listOf(
            "tt0232500",
            "tt0322259",
            "tt0463985",
            "tt1013752",
            "tt1596343",
            "tt1905041",
            "tt2820852",
            "tt4630562"
        )
        runBlocking {
            movieIds.forEach {
                val movieRest = getMovieByIdAsync(it)
                if (movieRest != null) obtainedMovies.add(movieRest)
            }
        }
        return obtainedMovies;
    }
}