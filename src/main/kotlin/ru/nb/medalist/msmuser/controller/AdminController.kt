package ru.nb.medalist.msmuser.controller

import org.keycloak.admin.client.CreatedResponseUtil
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import ru.nb.medalist.msmuser.dto.UserDto
import ru.nb.medalist.msmuser.keycloak.KeycloakUtils
//import ru.nb.medalist.msmuser.mq.MessageFuncActions
import java.util.*

data class Str(
	val data: String = ""
)

@RestController
@RequestMapping("admin")
class AdminController(
//	private val messageFuncActions: MessageFuncActions,
	private val keycloakUtils: KeycloakUtils
) {

	@GetMapping("info")
	suspend fun info(): Str {
		return Str("Test msm-user/admin/user: OK")
	}

	@PostMapping("/uuid")
	suspend fun getRandomUuid(@RequestBody id: Long): String {
		return if (id > 0) UUID.randomUUID().toString() + " : $id"
		else throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request")
	}

	/*
		@PostMapping("send")
		suspend fun sendMessage(@RequestBody id: Long) {
			messageFuncActions.sendNewUserMessage(id = id)
		}
	*/

	// добавление
	@PostMapping("add")
	suspend fun add(@RequestBody userDTO: UserDto): ResponseEntity<Any> {

		// проверка на обязательные параметры
		if (userDTO.id.isNotBlank()) {
			// id создается автоматически в БД (autoincrement), поэтому его передавать не нужно, иначе может быть конфликт уникальности значения
			return ResponseEntity("redundant param: id MUST be empty", HttpStatus.NOT_ACCEPTABLE)
		}

		// если передали пустое значение
		if (userDTO.email.isNullOrBlank()) {
			return ResponseEntity("missed param: email", HttpStatus.NOT_ACCEPTABLE)
		}
		if (userDTO.password.isBlank()) {
			return ResponseEntity("missed param: password", HttpStatus.NOT_ACCEPTABLE)
		}
		if (userDTO.username.isNullOrBlank()) {
			return ResponseEntity("missed param: username", HttpStatus.NOT_ACCEPTABLE)
		}

		// создаем пользователя
		val createdResponse = keycloakUtils.createKeycloakUser(userDTO) ?: run {
			return ResponseEntity("missed keycloak", HttpStatus.CONFLICT)
		}
		if (createdResponse.status == CONFLICT) {
			return ResponseEntity("user or email already exists " + userDTO.email, HttpStatus.CONFLICT)
		}

		// получаем его ID
		val userId = CreatedResponseUtil.getCreatedId(createdResponse)
		println("User created with userId: $userId")
		val defaultRoles = mutableListOf<String>()
		defaultRoles.add(USER_ROLE_NAME) // эта роль должна присутствовать в KC на уровне Realm
		defaultRoles.add("admin")
		keycloakUtils.addRoles(userId, defaultRoles)
		return ResponseEntity("User add successful: ${createdResponse.status}", HttpStatus.OK)
	}

	// получение объекта по id
	@PostMapping("/id")
	fun findById(@RequestBody userId: String): ResponseEntity<UserRepresentation> {
		return ResponseEntity.ok(keycloakUtils.findUserById(userId))
	}

	// получение уникального объекта по email
	@PostMapping("/search")
	fun search(@RequestBody email: String): ResponseEntity<List<UserRepresentation>> { // строго соответствие email
		println("---> email: $email")
		return ResponseEntity.ok(keycloakUtils.searchKeycloakUsers(email))
	}


	companion object {
		const val CONFLICT = 409 // если пользователь уже существует в KC и пытаемся создать такого же
		const val USER_ROLE_NAME = "user" // название роли из KC
	}


}