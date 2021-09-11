package com.example.cinema.api.dto

import com.example.cinema.persistence.model.RoomType
import java.math.BigDecimal

data class ReservationDto(
    val movieName: String,
    val tickets: Int,
    val room: RoomType,
    val totalPrice: BigDecimal
)