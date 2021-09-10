package com.example.cinema.api.request

import com.example.cinema.persistence.model.RoomType
import java.math.BigDecimal

data class MovieShowRequest(val movieId: Int, val room: RoomType, val time: String, val price: BigDecimal)