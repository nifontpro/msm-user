package ru.nb.medalist.msmuser.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
//import ru.nb.medalist.msmuser.mq.MessageFuncActions
import java.util.*

@RestController
@RequestMapping("auth")
class AuthController(
//	private val messageFuncActions: MessageFuncActions
) {

	@PostMapping("/uuid")
	fun getRandomUuid(@RequestBody id: Long): String {
		return if (id > 0) UUID.randomUUID().toString() + " : $id"
		else throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request")
	}

/*	@PostMapping("send")
	fun sendMessage(@RequestBody id: Long) {
		messageFuncActions.sendNewUserMessage(id = id)
	}*/
}