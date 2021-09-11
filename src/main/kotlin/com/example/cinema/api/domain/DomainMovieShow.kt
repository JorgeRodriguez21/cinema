package com.example.cinema.api.domain

import com.example.cinema.persistence.model.RoomType
import java.math.BigDecimal

data class DomainMovieShow(
    val roomId: Int,
    val roomName: RoomType,
    val movieTime: String,
    val price: BigDecimal
)