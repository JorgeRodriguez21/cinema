package com.example.cinema.service

import com.example.cinema.api.dto.MovieRestDto
import com.example.cinema.persistence.repository.MovieRepository
import com.example.cinema.utils.TestMovieBuilder.buildMovie
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.web.client.RestTemplate
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
internal class MovieRestServiceTest() {


    private val testScope = TestCoroutineScope()
    private val restTemplate: RestTemplate = mockk()
    private val movieRepository: MovieRepository = mockk()

    @AfterEach
    fun cleanUp() {
        testScope.cleanupTestCoroutines()
    }

    @Test
    fun `should call an external service with the right url to get a movie with an async call`() {
        runBlocking {
            val movieRestDto = MovieRestDto("", "", "", "", 0.0, "", listOf())
            val id = "123"
            val url =
                "https://www.omdbapi.com/?apikey=${com.example.cinema.service.MovieRestService.Companion.API_KEY}&i=${id}"
            every {

                restTemplate.getForObject(
                    url,
                    MovieRestDto::class.java
                )
            }.returns(movieRestDto)
            val movieRestService = MovieRestService(restTemplate, movieRepository)

            val actualMovie = movieRestService.getMovieByIdAsync(id)

            verify(exactly = 1) { restTemplate.getForObject(url, MovieRestDto::class.java) }
            assertThat(actualMovie).isNotNull
        }
    }

    @Test
    fun `should call an external service with the right url to get a null movie with an async call`() {
        runBlocking {
            val id = "123"
            val url =
                "https://www.omdbapi.com/?apikey=${com.example.cinema.service.MovieRestService.Companion.API_KEY}&i=${id}"
            every {

                restTemplate.getForObject(
                    url,
                    MovieRestDto::class.java
                )
            }.returns(null)
            val movieRestService = MovieRestService(restTemplate, movieRepository)

            val actualMovie = movieRestService.getMovieByIdAsync(id)

            verify(exactly = 1) { restTemplate.getForObject(url, MovieRestDto::class.java) }
            assertThat(actualMovie).isNull()
        }
    }

    @Test
    fun `should call an external service with the right url to get a movie`() {
        val movieRestDto = MovieRestDto("", "", "", "", 0.0, "", listOf())
        val id = "123"
        val url =
            "https://www.omdbapi.com/?apikey=${com.example.cinema.service.MovieRestService.Companion.API_KEY}&i=${id}"
        every {

            restTemplate.getForObject(
                url,
                MovieRestDto::class.java
            )
        }.returns(movieRestDto)
        val movieRestService = MovieRestService(restTemplate, movieRepository)

        val actualMovie = movieRestService.getMovieById(id)

        verify(exactly = 1) { restTemplate.getForObject(url, MovieRestDto::class.java) }
        assertThat(actualMovie).isNotNull
    }

    @Test
    fun `should call an external service with the right url to get a null movie`() {
        val id = "123"
        val url =
            "https://www.omdbapi.com/?apikey=${com.example.cinema.service.MovieRestService.Companion.API_KEY}&i=${id}"
        every {

            restTemplate.getForObject(
                url,
                MovieRestDto::class.java
            )
        }.returns(null)
        val movieRestService = MovieRestService(restTemplate, movieRepository)

        val actualMovie = movieRestService.getMovieById(id)

        verify(exactly = 1) { restTemplate.getForObject(url, MovieRestDto::class.java) }
        assertThat(actualMovie).isNull()
    }

    @Test
    fun `should load the list of movies from an external service`() = testScope.runBlockingTest {
        val movieRestDto = MovieRestDto("", "", "", "", 0.0, "", listOf())
        every { restTemplate.getForObject(any<String>(), MovieRestDto::class.java) }.returns(movieRestDto)
        val movieRestService = MovieRestService(restTemplate, movieRepository)

        val actualMovies = movieRestService.loadMovies()

        verify(exactly = 8) { restTemplate.getForObject(any<String>(), MovieRestDto::class.java) }
        assertThat(actualMovies.size).isEqualTo(8)
    }

    @Test
    fun `should load the list of movies from an external service avoiding nulls`() = testScope.runBlockingTest {
        val movieRestDto = MovieRestDto("", "", "", "", 0.0, "", listOf())
        every { restTemplate.getForObject(any<String>(), MovieRestDto::class.java) }.returns(movieRestDto)
        val notFoundId = "tt0463985";
        val url =
            "https://www.omdbapi.com/?apikey=${com.example.cinema.service.MovieRestService.Companion.API_KEY}&i=${notFoundId}"
        every { restTemplate.getForObject(url, MovieRestDto::class.java) }.returns(null)
        val movieRestService = MovieRestService(restTemplate, movieRepository)

        val actualMovies = movieRestService.loadMovies()

        verify(exactly = 8) { restTemplate.getForObject(any<String>(), MovieRestDto::class.java) }
        assertThat(actualMovies.size).isEqualTo(7)
    }

    @Test
    fun `should call the repository to get a movie by id`() {
        val id = 1
        val movie = buildMovie()
        every { movieRepository.findById(id) }.returns(Optional.of(movie))
        val movieRestService = MovieRestService(restTemplate, movieRepository)

        movieRestService.getMovieDetails(id)

        verify(exactly = 1) { movieRepository.findById(id) }
    }

    @Test
    fun `should throw if movie not found`() {
        val id = 1
        every { movieRepository.findById(id) }.returns(Optional.empty())
        val movieRestService = MovieRestService(restTemplate, movieRepository)

        assertThrows(NoSuchElementException::class.java) { movieRestService.getMovieDetails(id) }
    }

    @Test
    fun `should call the rest service to get a movie by imdbId`() {
        val id = 1
        val movie = buildMovie()
        every { movieRepository.findById(id) }.returns(Optional.of(movie))
        val movieRestDto = MovieRestDto("", "", "", "", 0.0, "", listOf())
        val url =
            "https://www.omdbapi.com/?apikey=${com.example.cinema.service.MovieRestService.Companion.API_KEY}&i=${movie.imdbId}"
        every {
            restTemplate.getForObject(
                url,
                MovieRestDto::class.java
            )
        }.returns(movieRestDto)
        val movieRestService = MovieRestService(restTemplate, movieRepository)

        movieRestService.getMovieDetails(id)

        verify(exactly = 1) {
            restTemplate.getForObject(
                url,
                MovieRestDto::class.java
            )
        }
    }

    @Test
    fun `should throw when the movie is not found by the rest service`() {
        val id = 1
        val movie = buildMovie()
        every { movieRepository.findById(id) }.returns(Optional.of(movie))
        val url =
            "https://www.omdbapi.com/?apikey=${com.example.cinema.service.MovieRestService.Companion.API_KEY}&i=${movie.imdbId}"
        every {
            restTemplate.getForObject(
                url,
                MovieRestDto::class.java
            )
        }.returns(null)
        val movieRestService = MovieRestService(restTemplate, movieRepository)

        assertThrows(NoSuchElementException::class.java) { movieRestService.getMovieDetails(id) }
    }

    @Test
    fun `should return a movieRestDto containing the right movie information`() {
        val id = 1
        val movie = buildMovie()
        every { movieRepository.findById(id) }.returns(Optional.of(movie))
        val movieRestDto = MovieRestDto("", "", "", "", 0.0, "", listOf())
        val url =
            "https://www.omdbapi.com/?apikey=${com.example.cinema.service.MovieRestService.Companion.API_KEY}&i=${movie.imdbId}"
        every {
            restTemplate.getForObject(
                url,
                MovieRestDto::class.java
            )
        }.returns(movieRestDto)
        val expectedMovieRestDto = movieRestDto.copy()
        expectedMovieRestDto.setComments(movie.reviews.toList())
        expectedMovieRestDto.calculateReviewRate(movie.reviews.toList())
        val movieRestService = MovieRestService(restTemplate, movieRepository)

        val actualMovieRestDto = movieRestService.getMovieDetails(id)

        assertThat(actualMovieRestDto).usingRecursiveComparison().isEqualTo(expectedMovieRestDto)
    }
}