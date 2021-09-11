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
        val user = User(1, "", "", Role.ADMIN)
        val review = Review(1, 5, "none", user, movie)
        val review2 = Review(1, 3, "none1", user, movie)
        val review3 = Review(1, 1, "none2", user, movie)
        val review4 = Review(1, 2, "none3", user, movie)
        val reviews = hashSetOf(review, review2, review3, review4)
        movie.moviesRooms.add(movieRoom)
        movie.moviesRooms.add(movieRoom2)
        movie.reviews = reviews
        return movie
    }
}