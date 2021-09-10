package com.example.cinema.service

import com.example.cinema.api.request.MovieShowRequest
import com.example.cinema.persistence.model.*
import com.example.cinema.persistence.repository.MovieRepository
import com.example.cinema.persistence.repository.RoomRepository
import io.mockk.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.*

internal class MovieServiceTest {

    private val roomRepository: RoomRepository = mockk()
    private val movieRepository: MovieRepository = mockk()

    @BeforeEach
    fun setup() {
        clearAllMocks()
    }

    @Test
    fun `should call repository to get the room`() {
        val movieShowRequest = MovieShowRequest(1, RoomType.NORMAL_3D, "3:30", BigDecimal.TEN)
        val room = Room(1, RoomType.NORMAL_3D)
        every { roomRepository.findByType(any()) }.returns(room)
        val movieFromDatabase = Movie(1, "movie", "1234")
        every { movieRepository.findById(any()) }.returns(Optional.of(movieFromDatabase))
        val movieService = MovieService(roomRepository, movieRepository)
        every { movieRepository.save(any()) }.returns(movieFromDatabase)

        movieService.saveShowTimes(movieShowRequest)

        verify(exactly = 1) { roomRepository.findByType(any()) }
    }

    @Test
    fun `should call the repository to find the movie`() {
        val movieShowRequest = MovieShowRequest(1, RoomType.NORMAL_3D, "3:30", BigDecimal.TEN)
        val room = Room(1, RoomType.NORMAL_3D)
        every { roomRepository.findByType(any()) }.returns(room)
        val movieFromDatabase = Movie(1, "movie", "1234")
        every { movieRepository.findById(any()) }.returns(Optional.of(movieFromDatabase))
        val movieService = MovieService(roomRepository, movieRepository)
        every { movieRepository.save(any()) }.returns(movieFromDatabase)

        movieService.saveShowTimes(movieShowRequest)

        verify(exactly = 1) { movieRepository.findById(any()) }
    }

    @Test
    fun `should throw if the movie is not found`() {
        val movieShowRequest = MovieShowRequest(1, RoomType.NORMAL_3D, "3:30", BigDecimal.TEN)
        val room = Room(1, RoomType.NORMAL_3D)
        every { roomRepository.findByType(any()) }.returns(room)
        every { movieRepository.findById(any()) }.returns(Optional.empty())
        val movieService = MovieService(roomRepository, movieRepository)

        val exception =
            assertThrows(NoSuchElementException::class.java) { movieService.saveShowTimes(movieShowRequest) }
    }

    @Test
    fun `should save the expected show movie data`() {
        val movieShowRequest = MovieShowRequest(1, RoomType.NORMAL_3D, "3:30", BigDecimal.TEN)
        val roomFromDatabase = Room(1, RoomType.NORMAL_3D)
        every { roomRepository.findByType(movieShowRequest.room) }.returns(roomFromDatabase)
        val movieFromDatabase = Movie(1, "movie", "1234")
        every { movieRepository.findById(any()) }.returns(Optional.of(movieFromDatabase))
        val movieRoomKey = MovieRoomKey(1, 1)
        val movieRoom =
            MovieRoom(movieRoomKey, movieFromDatabase, roomFromDatabase, movieShowRequest.price, movieShowRequest.time)
        val slot = slot<Movie>()
        val expectedMovieToBeSaved = Movie(1, "movie", "1234", moviesRooms = hashSetOf(movieRoom))
        every { movieRepository.save(capture(slot)) }.returns(Movie(1, "movie", "1234"))
        val movieService = MovieService(roomRepository, movieRepository)

        movieService.saveShowTimes(movieShowRequest)

        verify(exactly = 1) { movieRepository.save(any()) }
        assertThat(expectedMovieToBeSaved).isEqualTo(slot.captured)
    }
}