package com.example.cinema.persistence.repository

import com.example.cinema.persistence.model.Room
import com.example.cinema.persistence.model.RoomType
import org.springframework.data.repository.CrudRepository

interface RoomRepository : CrudRepository<Room, Int> {
    fun findByType(type: RoomType): Room
}