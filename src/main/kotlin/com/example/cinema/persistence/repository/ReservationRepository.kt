package com.example.cinema.persistence.repository

import com.example.cinema.persistence.model.Reservation
import org.springframework.data.repository.CrudRepository

interface ReservationRepository : CrudRepository<Reservation, Int>