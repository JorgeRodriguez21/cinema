package com.example.cinema.api.request

data class MovieReservationRequest(
    val movieId: Int,
    val roomId: Int,
    val numberOfTickets: Int,
    var username: String = ""
)