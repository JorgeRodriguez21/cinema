package com.example.cinema.utils

import com.example.cinema.persistence.model.*
import java.math.BigDecimal

object TestMovieBuilder {
    fun buildMovie(): Movie {
        val movie = Movie(1, "movie", "1234")
        val room = Room(1, RoomType.VIP_3D)
        val room2 = Room(2, RoomType.NORMAL)
        val movieRoom = MovieRoom(MovieRoomKey(1, 1), movie, room, BigDecimal.TEN, "15:30")
        val movieRoom2 = MovieRoom(MovieRoomKey(1, 2), movie, room2, BigDecimal.ONE, "16:30")
        movie.moviesRooms.add(movieRoom)
        movie.moviesRooms.add(movieRoom2)
        return movie
    }
}