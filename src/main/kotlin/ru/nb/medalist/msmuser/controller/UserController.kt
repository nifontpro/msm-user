package ru.nb.medalist.msmuser.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*

@RestController
@RequestMapping("user")
class UserController
//	(private val messageFuncActions: MessageFuncActions)
{

	@GetMapping("test")
	suspend fun test() = "Test user endpoint"

	@PostMapping("/uuid")
	fun getRandomUuid(@RequestBody id: Long): String {
		return if (id > 0) UUID.randomUUID().toString() + " : $id"
		else throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request")
	}

	@PostMapping("data")
	suspend fun getData(
		@RequestBody body: RS? = null
	): RS {
//		return if (x % 3 == 0) throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "AT timeout")
		return RS(res = "User data valid, body: ${body?.res}")
	}

	/*	@PostMapping("send")
		fun sendMessage(@RequestBody id: Long) {
			messageFuncActions.sendNewUserMessage(id = id)
		}*/
}

data class RS(
	val res: String
)