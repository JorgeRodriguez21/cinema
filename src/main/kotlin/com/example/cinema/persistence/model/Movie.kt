package com.example.cinema.persistence.model

import javax.persistence.*

@Entity
@Table(name = "movie")
data class Movie(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int?,
    val title: String,
    @Column(name = "imdb_id", unique = true)
    val imdbId: String,
    @OneToMany(mappedBy = "movie", fetch = FetchType.EAGER)
    var reviews: Set<Review> = setOf(),
    @OneToMany(mappedBy = "movie", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val moviesRooms: MutableSet<MovieRoom> = mutableSetOf()


) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Movie

        if (id != other.id) return false
        if (title != other.title) return false
        if (imdbId != other.imdbId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + title.hashCode()
        result = 31 * result + imdbId.hashCode()
        return result
    }


}