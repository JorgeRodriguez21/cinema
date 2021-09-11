package com.example.cinema.api.domain

import com.example.cinema.persistence.model.Movie

data class DomainMovie(
    val movieId: Int,
    val movieName: String,
    val shows: List<DomainMovieShow>
) {
    companion object {
        fun fromModel(movie: Movie): DomainMovie {
            val movieDomains = movie.moviesRooms.map { DomainMovieShow(it.id.roomId, it.room.type, it.time, it.price) }
            return DomainMovie(movie.id!!, movie.title, movieDomains)
        }
    }
}