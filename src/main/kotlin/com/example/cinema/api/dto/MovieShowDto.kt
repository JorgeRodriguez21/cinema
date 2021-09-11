package com.example.cinema.api.dto

import com.example.cinema.persistence.model.RoomType
import java.math.BigDecimal

data class MovieShowDto(
    val roomId: Int,
    val roomName: RoomType,
    val movieTime: String,
    val price: BigDecimal
)