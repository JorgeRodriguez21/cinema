package com.example.cinema.service.persistence

import com.example.cinema.api.dto.ReservationDto
import com.example.cinema.api.request.MovieReservationRequest
import com.example.cinema.persistence.model.MovieRoomKey
import com.example.cinema.persistence.model.Reservation
import com.example.cinema.persistence.repository.MovieRoomRepository
import com.example.cinema.persistence.repository.ReservationRepository
import com.example.cinema.persistence.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class ReservationService @Autowired constructor(
    private val userRepository: UserRepository,
    private val movieRoomRepository: MovieRoomRepository,
    private val reservationRepository: ReservationRepository
) {
    fun addReservation(movieRequest: MovieReservationRequest): ReservationDto {
        val user = userRepository.findByUsername(movieRequest.username) ?: throw NoSuchElementException("Invalid user")
        val movieRoomKey = MovieRoomKey(movieRequest.movieId, movieRequest.roomId)
        val optionalMovieRoom = movieRoomRepository.findById(movieRoomKey)
        if (optionalMovieRoom.isEmpty) {
            throw NoSuchElementException("No available shows for that movie and room");
        }
        val movieRoom = optionalMovieRoom.get()
        val reservation = Reservation(null, movieRequest.numberOfTickets, user, movieRoom)
        reservationRepository.save(reservation)
        return ReservationDto(
            movieRoom.movie.title, movieRequest.numberOfTickets, movieRoom.room.type, movieRoom.price.multiply(
                BigDecimal(movieRequest.numberOfTickets)
            )
        )
    }
}