package com.example.cinema.api.controller

import com.example.cinema.api.dto.ReservationDto
import com.example.cinema.api.request.MovieReservationRequest
import com.example.cinema.service.persistence.ReservationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.CurrentSecurityContext
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/reservation")
class ReservationController @Autowired constructor(private val service: ReservationService) {

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    fun createShowMovieTime(
        @CurrentSecurityContext(expression = "authentication?.name") username: String,
        @RequestBody request: MovieReservationRequest
    ): ReservationDto {
        request.username = username
        return service.addReservation(request)
    }
}
