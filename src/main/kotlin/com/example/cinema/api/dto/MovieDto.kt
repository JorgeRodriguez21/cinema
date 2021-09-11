package com.example.cinema.api.dto

import com.example.cinema.persistence.model.Movie

data class MovieDto(
    val movieId: Int,
    val movieName: String,
    val shows: List<MovieShowDto>
) {
    companion object {
        fun fromModel(movie: Movie): MovieDto {
            val movieDtos = movie.moviesRooms.map { MovieShowDto(it.id.roomId, it.room.type, it.time, it.price) }
            return MovieDto(movie.id!!, movie.title, movieDtos)
        }
    }
}