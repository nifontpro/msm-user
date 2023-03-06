package ru.nb.medalist.msmuser.controller

//import ru.nb.medalist.msmuser.mq.MessageFuncActions
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*

@RestController
@RequestMapping("user")
class UserController(
//	private val messageFuncActions: MessageFuncActions
) {

	@PostMapping("/uuid")
	fun getRandomUuid(@RequestBody id: Long): String {
		return if (id > 0) UUID.randomUUID().toString() + " : $id"
		else throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request")
	}

	@GetMapping("data")
	fun getData(): RS {
		return RS(res = "User data valid, body:")
	}

	/*	@PostMapping("send")
		fun sendMessage(@RequestBody id: Long) {
			messageFuncActions.sendNewUserMessage(id = id)
		}*/
}

data class RS(
	val res: String
)