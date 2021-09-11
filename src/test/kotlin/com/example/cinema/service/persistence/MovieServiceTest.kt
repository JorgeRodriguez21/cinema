package com.example.cinema.service.persistence

import com.example.cinema.api.dto.MovieDto
import com.example.cinema.api.request.MovieShowDeleteRequest
import com.example.cinema.api.request.MovieShowRequest
import com.example.cinema.persistence.model.*
import com.example.cinema.persistence.repository.MovieRepository
import com.example.cinema.persistence.repository.MovieRoomRepository
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
    private val movieRoomRepository: MovieRoomRepository = mockk()

    @BeforeEach
    fun setup() {
        clearAllMocks()
    }

    @Test
    fun `should call repository to get the room`() {
        val movieShowRequest = MovieShowRequest(1, RoomType.NORMAL_3D, "3:30", BigDecimal.TEN)
        val roomFromDatabase = Room(1, RoomType.NORMAL_3D)
        every { roomRepository.findByType(any()) }.returns(roomFromDatabase)
        val movieFromDatabase = Movie(1, "movie", "1234")
        every { movieRepository.findById(any()) }.returns(Optional.of(movieFromDatabase))
        val movieService = MovieService(roomRepository, movieRepository, movieRoomRepository)
        every { movieRepository.save(any()) }.returns(movieFromDatabase)
        val movieRoomKey = MovieRoomKey(1, 1)
        val movieRoom =
            MovieRoom(movieRoomKey, movieFromDatabase, roomFromDatabase, movieShowRequest.price, movieShowRequest.time)
        every { movieRoomRepository.save(any()) }.returns(movieRoom)

        movieService.saveShowTimes(movieShowRequest)

        verify(exactly = 1) { roomRepository.findByType(any()) }
    }

    @Test
    fun `should call the repository to find the movie`() {
        val movieShowRequest = MovieShowRequest(1, RoomType.NORMAL_3D, "3:30", BigDecimal.TEN)
        val roomFromDatabase = Room(1, RoomType.NORMAL_3D)
        every { roomRepository.findByType(any()) }.returns(roomFromDatabase)
        val movieFromDatabase = Movie(1, "movie", "1234")
        every { movieRepository.findById(any()) }.returns(Optional.of(movieFromDatabase))
        val movieService = MovieService(roomRepository, movieRepository, movieRoomRepository)
        every { movieRepository.save(any()) }.returns(movieFromDatabase)
        val movieRoomKey = MovieRoomKey(1, 1)
        val movieRoom =
            MovieRoom(movieRoomKey, movieFromDatabase, roomFromDatabase, movieShowRequest.price, movieShowRequest.time)
        every { movieRoomRepository.save(any()) }.returns(movieRoom)

        movieService.saveShowTimes(movieShowRequest)

        verify(exactly = 1) { movieRepository.findById(any()) }
    }

    @Test
    fun `should throw if the movie is not found`() {
        val movieShowRequest = MovieShowRequest(1, RoomType.NORMAL_3D, "3:30", BigDecimal.TEN)
        val room = Room(1, RoomType.NORMAL_3D)
        every { roomRepository.findByType(any()) }.returns(room)
        every { movieRepository.findById(any()) }.returns(Optional.empty())
        val movieService = MovieService(roomRepository, movieRepository, movieRoomRepository)

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
        val movieService = MovieService(roomRepository, movieRepository, movieRoomRepository)
        every { movieRoomRepository.save(movieRoom) }.returns(movieRoom)

        movieService.saveShowTimes(movieShowRequest)

        verify(exactly = 1) { movieRepository.save(any()) }
        assertThat(expectedMovieToBeSaved).isEqualTo(slot.captured)
    }

    @Test
    fun `should call repository to get all available movies`() {
        val movieFromDatabase = Movie(1, "movie", "1234")
        every { movieRepository.findAll() }.returns(listOf(movieFromDatabase))
        val movieService = MovieService(roomRepository, movieRepository, movieRoomRepository)

        movieService.retrieveAllMoviesInformation()

        verify(exactly = 1) { movieRepository.findAll() }
    }

    @Test
    fun `should return a list of movie DTOs`() {
        val movieFromDatabase = Movie(1, "movie", "1234")
        val room = Room(1, RoomType.VIP_3D)
        val room2 = Room(2, RoomType.NORMAL)
        val movieRoom = MovieRoom(MovieRoomKey(1, 1), movieFromDatabase, room, BigDecimal.TEN, "15:30")
        val movieRoom2 = MovieRoom(MovieRoomKey(1, 2), movieFromDatabase, room2, BigDecimal.ONE, "16:30")
        movieFromDatabase.moviesRooms.add(movieRoom)
        movieFromDatabase.moviesRooms.add(movieRoom2)
        val expectedMovieDto = MovieDto.fromModel(movieFromDatabase)
        every { movieRepository.findAll() }.returns(listOf(movieFromDatabase))
        val movieService = MovieService(roomRepository, movieRepository, movieRoomRepository)

        val actualMovies = movieService.retrieveAllMoviesInformation()

        assertThat(listOf(expectedMovieDto)).usingRecursiveComparison().isEqualTo(actualMovies)
    }

    @Test
    fun `should call repository to delete a movie room by id`() {
        val movieShowDeleteRequest = MovieShowDeleteRequest(1, 1)
        val movieRoomKey = MovieRoomKey(1, 1)
        justRun { movieRoomRepository.deleteById(movieRoomKey) }
        val movieService = MovieService(roomRepository, movieRepository, movieRoomRepository)

        movieService.deleteMovieShow(movieShowDeleteRequest)

        verify(exactly = 1) { movieRoomRepository.deleteById(movieRoomKey) }

    }

    @Test
    fun `should call repository get a movie by id`() {
        every { movieRepository.findById(1) }.returns(Optional.empty())
        val movieService = MovieService(roomRepository, movieRepository, movieRoomRepository)

        movieService.getMovieById(1)

        verify(exactly = 1) { movieRepository.findById(1) }

    }

    @Test
    fun `should return the result of the repository call`() {
        every { movieRepository.findById(1) }.returns(Optional.empty())
        val movieService = MovieService(roomRepository, movieRepository, movieRoomRepository)

        val result = movieService.getMovieById(1)

        assertThat(result).isEmpty
    }

    @Test
    fun `should return the result of the repository call when optional is not empty`() {
        val movieFromDatabase = Movie(1, "movie", "1234")
        every { movieRepository.findById(1) }.returns(Optional.of(movieFromDatabase))
        val movieService = MovieService(roomRepository, movieRepository, movieRoomRepository)

        val result = movieService.getMovieById(1)

        assertThat(result.get()).usingRecursiveComparison().isEqualTo(movieFromDatabase)
    }
}