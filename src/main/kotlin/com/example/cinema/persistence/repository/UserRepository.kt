package com.example.cinema.persistence.repository

import com.example.cinema.persistence.model.User
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, Int> {
    fun findByUsername(username: String): User?
}