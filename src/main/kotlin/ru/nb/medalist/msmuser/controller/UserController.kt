package ru.nb.medalist.msmuser.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import ru.nb.medalist.msmuser.utils.JwtUtils
import java.security.Principal
import java.util.*

@RestController
@RequestMapping("user")
class UserController(
//	(private val messageFuncActions: MessageFuncActions)
	private val jwtUtils: JwtUtils
) {

	@GetMapping("test")
	suspend fun test() = "Test user endpoint"

	@PostMapping("/uuid")
	fun getRandomUuid(@RequestBody id: Long): String {
		return if (id > 0) UUID.randomUUID().toString() + " : $id"
		else throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request")
	}

	@PostMapping("data")
	suspend fun getData(
		@RequestBody body: RS? = null,
		principal: Principal,
		@RequestHeader(name = AUTHORIZATION) bearerToken: String
	): RS {
		println(principal.name)
		val authData = jwtUtils.decodeBearerJwt(bearerToken)
		println(authData)
		return RS(res = "User data valid, body: ${body?.res}, name: ${authData.name}")
	}

	/*	@PostMapping("send")
		fun sendMessage(@RequestBody id: Long) {
			messageFuncActions.sendNewUserMessage(id = id)
		}*/

	companion object {
		private const val AUTHORIZATION = "Authorization"
	}
}

data class RS(
	val res: String
)