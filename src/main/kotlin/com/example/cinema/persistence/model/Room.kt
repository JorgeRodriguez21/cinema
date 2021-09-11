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
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Room

        if (id != other.id) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + type.hashCode()
        return result
    }
}
