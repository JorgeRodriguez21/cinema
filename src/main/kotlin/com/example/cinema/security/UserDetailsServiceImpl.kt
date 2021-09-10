package com.example.cinema.security

import com.example.cinema.persistence.model.User
import com.example.cinema.persistence.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.security.core.userdetails.User as UserDetails

@Service
class UserDetailsServiceImpl @Autowired constructor(val userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user: User = userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("Invalid username or password")
        val grantedAuthorities: MutableSet<GrantedAuthority> = HashSet(emptySet())
        grantedAuthorities.add(SimpleGrantedAuthority(user.role.toString()))
        return UserDetails(user.username, user.password, grantedAuthorities)
    }
}