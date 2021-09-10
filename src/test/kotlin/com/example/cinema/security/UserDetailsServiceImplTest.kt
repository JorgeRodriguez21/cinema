package com.example.cinema.security

import com.example.cinema.persistence.model.Role
import com.example.cinema.persistence.model.User
import com.example.cinema.persistence.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.core.userdetails.User as UserDetails

internal class UserDetailsServiceImplTest {

    private val userRepository: UserRepository = mockk()

    @Test
    fun `should call repository to get the user by username`() {
        val userDetailsService = UserDetailsServiceImpl(userRepository)
        every { userRepository.findByUsername(any()) }.returns(User(1, "username", "pass", Role.ADMIN, hashSetOf()))

        userDetailsService.loadUserByUsername("")

        verify(exactly = 1) { userRepository.findByUsername(any()) }
    }

    @Test
    fun `should return  an authenticated user when user is found in the database`() {
        val userDetailsService = UserDetailsServiceImpl(userRepository)
        val foundUserInDb = User(
            1,
            "username",
            "pass",
            Role.ADMIN,
            hashSetOf()
        )
        every { userRepository.findByUsername("username") }.returns(
            foundUserInDb
        )
        val grantedAuthorities: MutableSet<GrantedAuthority> = HashSet(emptySet())
        grantedAuthorities.add(SimpleGrantedAuthority(foundUserInDb.role.toString()))
        val expectedUser = UserDetails(foundUserInDb.username, foundUserInDb.password, grantedAuthorities)

        val actualUser = userDetailsService.loadUserByUsername("username")

        assertThat(actualUser).usingRecursiveComparison().isEqualTo(expectedUser)
    }

    @Test
    fun `should throw when user is not found in the db`() {
        val userDetailsService = UserDetailsServiceImpl(userRepository)
        every { userRepository.findByUsername(any()) }.returns(
            null
        )

        val exception =
            assertThrows(UsernameNotFoundException::class.java) { userDetailsService.loadUserByUsername("username") }

        assertThat(exception.message).isEqualTo("Invalid username or password")
    }
}