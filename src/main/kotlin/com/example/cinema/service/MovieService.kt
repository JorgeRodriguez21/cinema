package com.example.cinema.service

import com.example.cinema.api.domain.DomainMovie
import com.example.cinema.api.request.MovieShowDeleteRequest
import com.example.cinema.api.request.MovieShowRequest
import com.example.cinema.persistence.model.Movie
import com.example.cinema.persistence.model.MovieRoom
import com.example.cinema.persistence.model.MovieRoomKey
import com.example.cinema.persistence.model.Room
import com.example.cinema.persistence.repository.MovieRepository
import com.example.cinema.persistence.repository.MovieRoomRepository
import com.example.cinema.persistence.repository.RoomRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MovieService @Autowired constructor(
    private val roomRepository: RoomRepository,
    private val movieRepository: MovieRepository,
    private val movieRoomRepository: MovieRoomRepository
) {
    fun saveShowTimes(movieShowRequest: MovieShowRequest) {
        val foundRoom = roomRepository.findByType(movieShowRequest.room)
        val foundMovie: Movie = movieRepository.findById(movieShowRequest.movieId).orElseThrow()
        movieRepository.save(foundMovie)
        saveOrUpdateMovieRooms(foundMovie, foundRoom, movieShowRequest)
    }

    private fun saveOrUpdateMovieRooms(
        movie: Movie,
        room: Room,
        movieShowRequest: MovieShowRequest
    ) {
        val movieRoomKey = MovieRoomKey(movie.id!!, room.id)
        val movieRoom =
            MovieRoom(movieRoomKey, movie, room, movieShowRequest.price, movieShowRequest.time)
        movieRoomRepository.save(movieRoom)
    }

    fun retrieveAllMoviesInformation(): List<DomainMovie> {
        val movies = movieRepository.findAll()
        return movies.map(DomainMovie::fromModel)
    }

    fun deleteMovieShow(movieShowDeleteRequest: MovieShowDeleteRequest) {
        val movieRoomKey = MovieRoomKey(movieShowDeleteRequest.movieId, movieShowDeleteRequest.roomId)
        movieRoomRepository.deleteById(movieRoomKey)
    }
}