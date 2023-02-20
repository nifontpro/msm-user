package ru.nb.medalist.msmuser.dto

data class UserDto(
	val id: String = "",
	val email: String? = null,
	val username: String? = null,
	val password: String = "",
)
