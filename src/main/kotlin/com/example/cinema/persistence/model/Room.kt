package com.example.cinema.persistence.model

import javax.persistence.*

@Entity
@Table(name = "room")
data class Room(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,
    @Column(name = "\"type\"")
    @Enumerated(EnumType.STRING)
    val type: RoomType,
    @OneToMany(mappedBy = "room")
    val moviesRooms: Set<MovieRoom> = hashSetOf()
)
