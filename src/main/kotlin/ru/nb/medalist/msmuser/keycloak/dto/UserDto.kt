package ru.nb.medalist.msmuser.keycloak.dto

data class UserDto(
	val id: String,
	val email: String,
	val username: String,
	val password: String,
)
