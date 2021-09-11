package com.example.cinema.persistence.model

import javax.persistence.*

@Entity
@Table(name = "\"user\"")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int?,
    val username: String,
    val password: String,
    @Enumerated(EnumType.STRING)
    val role: Role,
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private val reviews: Set<Review> = hashSetOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false
        if (username != other.username) return false
        if (password != other.password) return false
        if (role != other.role) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + username.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + role.hashCode()
        return result
    }

    override fun toString(): String {
        return "User(id=$id, username='$username', role=$role)"
    }
}
