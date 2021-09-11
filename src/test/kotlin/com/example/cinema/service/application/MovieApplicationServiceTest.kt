package com.example.cinema.service.application

import com.example.cinema.api.dto.MovieRestDto
import com.example.cinema.persistence.repository.MovieRepository
import com.example.cinema.persistence.repository.MovieRoomRepository
import com.example.cinema.persistence.repository.RoomRepository
import com.example.cinema.service.persistence.MovieService
import com.example.cinema.service.rest.MovieRestService
import com.example.cinema.utils.TestMovieBuilder
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

internal class MovieApplicationServiceTest {

    private val movieService: MovieService = mockk()
    private val movieRestService: MovieRestService = mockk()

    @BeforeEach
    fun setup() {
        clearAllMocks()
    }

    @Test
    fun `should call the repository to get a movie by id`() {
        val id = 1
        val movie = TestMovieBuilder.buildMovie()
        every { movieService.getMovieById(id) }.returns(Optional.of(movie))
        val movieApplicationService = MovieApplicationService(movieService, movieRestService)
        val movieRestDto = MovieRestDto("", "", "", "", 0.0, "", listOf())
        every {
            movieRestService.getMovieById(movie.imdbId)
        }.returns(movieRestDto)

        movieApplicationService.getMovieDetails(id)

        verify(exactly = 1) { movieService.getMovieById(id) }
    }

    @Test
    fun `should throw if movie not found`() {
        val id = 1
        every { movieService.getMovieById(id) }.returns(Optional.empty())
        val movieApplicationService = MovieApplicationService(movieService, movieRestService)

        Assertions.assertThrows(NoSuchElementException::class.java) { movieApplicationService.getMovieDetails(id) }
    }

    @Test
    fun `should call the rest service to get a movie by imdbId`() {
        val id = 1
        val movie = TestMovieBuilder.buildMovie()
        every { movieService.getMovieById(id) }.returns(Optional.of(movie))
        val movieRestDto = MovieRestDto("", "", "", "", 0.0, "", listOf())
        every { movieRestService.getMovieById(movie.imdbId) }.returns(movieRestDto)
        val movieApplicationService = MovieApplicationService(movieService, movieRestService)

        movieApplicationService.getMovieDetails(id)

        verify(exactly = 1) { movieRestService.getMovieById(movie.imdbId) }
    }

    @Test
    fun `should throw when the movie is not found by the rest service`() {
        val id = 1
        val movie = TestMovieBuilder.buildMovie()
        every { movieService.getMovieById(id) }.returns(Optional.of(movie))

        every {
            movieRestService.getMovieById(movie.imdbId)
        }.returns(null)

        val movieApplicationService = MovieApplicationService(movieService, movieRestService)

        Assertions.assertThrows(NoSuchElementException::class.java) { movieApplicationService.getMovieDetails(id) }
    }

    @Test
    fun `should return a movieRestDto containing the right movie information`() {
        val id = 1
        val movie = TestMovieBuilder.buildMovie()
        every { movieService.getMovieById(id) }.returns(Optional.of(movie))
        val movieRestDto = MovieRestDto("", "", "", "", 0.0, "", listOf())
        every {
            movieRestService.getMovieById(movie.imdbId)
        }.returns(movieRestDto)
        val expectedMovieRestDto = movieRestDto.copy()
        expectedMovieRestDto.setComments(movie.reviews.toList())
        expectedMovieRestDto.calculateReviewRate(movie.reviews.toList())
        val movieApplicationService = MovieApplicationService(movieService, movieRestService)

        val actualMovieRestDto = movieApplicationService.getMovieDetails(id)

        assertThat(actualMovieRestDto).usingRecursiveComparison().isEqualTo(expectedMovieRestDto)
    }

}