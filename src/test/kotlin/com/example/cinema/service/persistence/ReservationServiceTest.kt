package com.example.cinema.service.persistence

import com.example.cinema.api.dto.ReservationDto
import com.example.cinema.api.request.MovieReservationRequest
import com.example.cinema.persistence.model.*
import com.example.cinema.persistence.repository.MovieRoomRepository
import com.example.cinema.persistence.repository.ReservationRepository
import com.example.cinema.persistence.repository.UserRepository
import com.example.cinema.utils.TestMovieBuilder.buildMovie
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.*

internal class ReservationServiceTest {

    private val userRepository: UserRepository = mockk()
    private val movieRoomRepository: MovieRoomRepository = mockk()
    private val reservationRepository: ReservationRepository = mockk()

    @Test
    fun `should call the repository to get the user by username`() {
        val movieRequest = MovieReservationRequest(1, 1, 2, "username")
        val user = User(1, "", "", Role.USER)
        every { userRepository.findByUsername(any()) } returns user
        val reservationService = ReservationService(userRepository, movieRoomRepository, reservationRepository)
        val movie = buildMovie()
        val room = Room(1, RoomType.NORMAL)
        every { userRepository.findByUsername(any()) } returns user
        val showKey = MovieRoomKey(1, 1)
        val foundMovieShow = MovieRoom(showKey, movie, room, BigDecimal.TEN, "05:30")
        every { movieRoomRepository.findById(any()) }.returns(Optional.of(foundMovieShow))
        val reservation = Reservation(null, 2, user, foundMovieShow)
        every { reservationRepository.save(any()) }.returns(reservation)

        reservationService.addReservation(movieRequest)

        verify(exactly = 1) { userRepository.findByUsername("username") }

    }

    @Test
    fun `should throw when user is not found`() {
        val movieRequest = MovieReservationRequest(1, 1, 2, "username")
        every { userRepository.findByUsername(any()) } returns null
        val reservationService = ReservationService(userRepository, movieRoomRepository, reservationRepository)

        val actualException =
            assertThrows(NoSuchElementException::class.java) { reservationService.addReservation(movieRequest) }

        assertThat(actualException.message).isEqualTo("Invalid user")
    }

    @Test
    fun `should call the repository find a show by id`() {
        val movieRequest = MovieReservationRequest(1, 1, 2, "username")
        val user = User(1, "", "", Role.USER)
        val movie = buildMovie()
        val room = Room(1, RoomType.NORMAL)
        every { userRepository.findByUsername(any()) } returns user
        val showKey = MovieRoomKey(1, 1)
        val foundMovieShow = MovieRoom(showKey, movie, room, BigDecimal.TEN, "05:30")
        every { movieRoomRepository.findById(showKey) }.returns(Optional.of(foundMovieShow))
        val reservation = Reservation(null, 2, user, foundMovieShow)
        every { reservationRepository.save(any()) }.returns(reservation)
        val reservationService = ReservationService(userRepository, movieRoomRepository, reservationRepository)

        reservationService.addReservation(movieRequest)

        verify(exactly = 1) { movieRoomRepository.findById(showKey) }
    }

    @Test
    fun `should throw when a movieRoom is not found`() {
        val movieRequest = MovieReservationRequest(1, 1, 2, "username")
        val user = User(1, "", "", Role.USER)
        every { userRepository.findByUsername(any()) } returns user
        val showKey = MovieRoomKey(1, 1)
        every { movieRoomRepository.findById(showKey) }.returns(Optional.empty())
        val reservationService = ReservationService(userRepository, movieRoomRepository, reservationRepository)

        val actualException =
            assertThrows(NoSuchElementException::class.java) { reservationService.addReservation(movieRequest) }

        assertThat(actualException.message).isEqualTo("No available shows for that movie and room")
    }

    @Test
    fun `should call the repository to save a reservation`() {
        val movieRequest = MovieReservationRequest(1, 1, 2, "username")
        val user = User(1, "", "", Role.USER)
        val movie = buildMovie()
        val room = Room(1, RoomType.NORMAL)
        every { userRepository.findByUsername(any()) } returns user
        val showKey = MovieRoomKey(1, 1)
        val foundMovieShow = MovieRoom(showKey, movie, room, BigDecimal.TEN, "05:30")
        every { movieRoomRepository.findById(showKey) }.returns(Optional.of(foundMovieShow))
        val reservation = Reservation(null, 2, user, foundMovieShow)
        every { reservationRepository.save(reservation) }.returns(reservation)
        val reservationService = ReservationService(userRepository, movieRoomRepository, reservationRepository)

        reservationService.addReservation(movieRequest)

        verify { reservationRepository.save(reservation) }
    }

    @Test
    fun `should return an object with the expected data`() {
        val movieRequest = MovieReservationRequest(1, 1, 2, "username")
        val user = User(1, "", "", Role.USER)
        val movie = buildMovie()
        val room = Room(1, RoomType.NORMAL)
        every { userRepository.findByUsername(any()) } returns user
        val showKey = MovieRoomKey(1, 1)
        val foundMovieShow = MovieRoom(showKey, movie, room, BigDecimal.TEN, "05:30")
        every { movieRoomRepository.findById(showKey) }.returns(Optional.of(foundMovieShow))
        val reservation = Reservation(null, 2, user, foundMovieShow)
        every { reservationRepository.save(reservation) }.returns(reservation)
        val expectedReservationDto = ReservationDto(
            movie.title, movieRequest.numberOfTickets, RoomType.NORMAL,
            BigDecimal(20)
        )
        val reservationService = ReservationService(userRepository, movieRoomRepository, reservationRepository)

        val actualReservationDto = reservationService.addReservation(movieRequest)

        assertThat(actualReservationDto).usingRecursiveComparison().isEqualTo(expectedReservationDto)
    }
}