package ru.nb.medalist.msmuser.mq

import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Service
import reactor.core.publisher.Sinks
import ru.nb.medalist.msmuser.mq.MessageFunc

// работа с каналами
@Service
// помогает реализовать отправку сообщения с помощью функц. кода - по требованию (только после вызова соотв. метода)
// каналы для обмена сообщениями

class MessageFuncActions(
	private val messageFunc: MessageFunc
) {

	// отправка сообщения
	fun sendNewUserMessage(id: Long) {
		// добавляем в слушателе новое сообщение
		messageFunc.innerBus.emitNext(MessageBuilder.withPayload(id).build(), Sinks.EmitFailureHandler.FAIL_FAST)
		println("Message sent: $id")
	}
}