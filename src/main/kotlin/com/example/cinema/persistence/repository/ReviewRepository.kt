package com.example.cinema.persistence.repository

import com.example.cinema.persistence.model.Review
import org.springframework.data.repository.CrudRepository

interface ReviewRepository : CrudRepository<Review, Int>