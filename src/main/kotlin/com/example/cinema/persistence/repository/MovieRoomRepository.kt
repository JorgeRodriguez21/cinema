package com.example.cinema.persistence.repository

import com.example.cinema.persistence.model.MovieRoom
import com.example.cinema.persistence.model.MovieRoomKey
import org.springframework.data.repository.CrudRepository

interface MovieRoomRepository : CrudRepository<MovieRoom, MovieRoomKey>