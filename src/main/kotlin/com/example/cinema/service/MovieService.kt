package com.example.cinema.service

import com.example.cinema.api.domain.DomainMovie
import com.example.cinema.api.request.MovieShowRequest
import com.example.cinema.persistence.model.Movie
import com.example.cinema.persistence.model.MovieRoom
import com.example.cinema.persistence.model.MovieRoomKey
import com.example.cinema.persistence.repository.MovieRepository
import com.example.cinema.persistence.repository.RoomRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MovieService @Autowired constructor(
    private val roomRepository: RoomRepository,
    private val movieRepository: MovieRepository
) {
    fun saveShowTimes(movieShowRequest: MovieShowRequest) {
        val foundRoom = roomRepository.findByType(movieShowRequest.room)
        val foundMovie: Movie = movieRepository.findById(movieShowRequest.movieId).orElseThrow()
        val movieRoomKey = MovieRoomKey(foundMovie.id!!, foundRoom.id)
        val movieRoom =
            MovieRoom(movieRoomKey, foundMovie, foundRoom, movieShowRequest.price, movieShowRequest.time)
        foundMovie.moviesRooms.add(movieRoom)
        movieRepository.save(foundMovie)
    }

    fun retrieveAllMoviesInformation(): List<DomainMovie> {
        val movies = movieRepository.findAll()
        return movies.map(DomainMovie::fromModel)
    }
}