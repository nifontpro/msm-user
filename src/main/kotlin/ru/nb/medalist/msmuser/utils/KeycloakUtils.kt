package ru.nb.medalist.msmuser.utils

import org.keycloak.OAuth2Constants
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.keycloak.admin.client.resource.RealmResource
import org.keycloak.admin.client.resource.UsersResource
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.RoleRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.nb.medalist.msmuser.dto.UserDto
import javax.annotation.PostConstruct
import javax.ws.rs.core.Response

@Service
class KeycloakUtils(
	// настройки из файла properties
	@Value("\${keycloak.auth-server-url}")
	private val serverURL: String,

	@Value("\${keycloak.realm}")
	private val realm: String,

	@Value("\${keycloak.resource}")
	private val clientID: String,

	@Value("\${keycloak.credentials.secret}")
	private val clientSecret: String,
) {

	// создание объектов KC - будет выполняться после инициализации Spring бина
	@PostConstruct
	fun initKeycloak(): Keycloak? {
		if (keycloak == null) { // создаем объект только 1 раз
			keycloak = KeycloakBuilder.builder()
				.realm(realm)
				.serverUrl(serverURL)
				.clientId(clientID)
				.clientSecret(clientSecret)
				.grantType(OAuth2Constants.CLIENT_CREDENTIALS)
				.build()
			realmResource = keycloak?.realm(realm)
			usersResource = realmResource?.users()
		}
		return keycloak
	}

	// создание пользователя для KC
	fun createKeycloakUser(userDTO: UserDto): Response? {

		// данные пароля - специальный объект-контейнер CredentialRepresentation
		val credentialRepresentation = createPasswordCredentials(userDTO.password)

		// данные пользователя (можете задавать или убирать любые поля - зависит от требуемого функционала)
		// специальный объект-контейнер UserRepresentation
		val kcUser = UserRepresentation().apply {
			username = userDTO.username
			credentials = listOf(credentialRepresentation)
			email = userDTO.email
			isEnabled = true
			isEmailVerified = false
		}

		// вызов KC (всю внутреннюю кухню за нас делает библиотека - формирует REST запросы, заполняет параметры и пр.)
		return usersResource?.create(kcUser)
	}

	// удаление пользователя для KC
	fun deleteKeycloakUser(userId: String?) {

		// получаем пользователя
		usersResource?.let {
			val uniqueUserResource = it[userId]
			uniqueUserResource.remove()
		}

	}

	// обновление пользователя для KC
	fun updateKeycloakUser(userDTO: UserDto) {

		// данные пароля - специальный объект-контейнер CredentialRepresentation
		val credentialRepresentation = createPasswordCredentials(userDTO.password)

		// какие поля обновляем
		val kcUser = UserRepresentation().apply {
			username = userDTO.username
			credentials = listOf(credentialRepresentation)
			email = userDTO.email
		}

		usersResource?.let {
			val uniqueUserResource = it[userDTO.id]
			uniqueUserResource.update(kcUser) // обновление
		}
	}

	// поиск уникального пользователя
	fun findUserById(userId: String?): UserRepresentation? {
		// получаем пользователя
		return usersResource?.let { it[userId].toRepresentation() }
	}

	// поиск пользователя по любым атрибутам (вхождение текста)
	fun searchKeycloakUsers(text: String?): List<UserRepresentation> {
		return usersResource?.searchByAttributes(text) ?: emptyList()
	}

	// добавление роли пользователю
	fun addRoles(userId: String?, roles: List<String>) {

		// список доступных ролей в Realm
		val kcRoles = mutableListOf<RoleRepresentation>()

		// преобразуем тексты в спец. объекты RoleRepresentation, который понятен для KC
		for (role in roles) {
			realmResource?.let {
				val roleRep = it.roles()[role].toRepresentation()
//				val roleRepresentation = RoleRepresentation()
//				roleRepresentation.name = role
				kcRoles.add(roleRep)
			}
		}

		usersResource?.let {
			// получаем пользователя
			val uniqueUserResource = it[userId]

			// и добавляем ему Realm-роли (т.е. роль добавится в общий список Roles)
			uniqueUserResource.roles().realmLevel().add(kcRoles)
		}

	}

	// данные о пароле
	private fun createPasswordCredentials(password: String): CredentialRepresentation {
		val passwordCredentials = CredentialRepresentation().apply {
			isTemporary = false // не нужно будет менять пароль после первого входа
			type = CredentialRepresentation.PASSWORD
			value = password
		}
		return passwordCredentials
	}

	companion object {
		private var keycloak: Keycloak? = null // ссылка на единственный экземпляр объекта KC
		private var realmResource: RealmResource? = null // доступ к API realm
		private var usersResource: UsersResource? = null // доступ к API для работы с пользователями
	}
}
