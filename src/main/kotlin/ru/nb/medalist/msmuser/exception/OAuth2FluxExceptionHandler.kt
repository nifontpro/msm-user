package ru.nb.medalist.msmuser.exception

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.server.ServerAuthenticationEntryPoint
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

data class ExceptionResponse(
	val type: String = "",
	val message: String = "",
	val exception: String = "",
)

@Component
class OAuth2FluxExceptionHandler : ServerAuthenticationEntryPoint {
	override fun commence(exchange: ServerWebExchange, exception: AuthenticationException): Mono<Void> {
		exchange.response.statusCode = HttpStatus.FORBIDDEN

		// https://www.baeldung.com/kotlin/jackson-kotlin
		val mapper = jacksonObjectMapper()
		val exceptionResponse = ExceptionResponse(
			type = exception::class.java.simpleName,
			message = exception.message.toString(),
			exception = exception.cause.toString()
		)

		val bytes: ByteArray = mapper.writeValueAsBytes(exceptionResponse)
		val buffer: DataBuffer = exchange.response.bufferFactory().wrap(bytes)

		return exchange.response.writeWith(Flux.just(buffer))
	}
}

// https://stackoverflow.com/questions/45211431/webexceptionhandler-how-to-write-a-body-with-spring-webflux
// https://newbedev.com/http-response-exception-handling-in-spring-5-reactive