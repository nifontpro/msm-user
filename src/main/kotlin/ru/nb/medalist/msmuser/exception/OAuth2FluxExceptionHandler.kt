package ru.nb.medalist.msmuser.exception

import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.server.ServerAuthenticationEntryPoint
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.nio.charset.StandardCharsets


// обрабатывает ошибки oauth2 (авторизация и пр.)
// web
//class OAuth2FluxExceptionHandler : AuthenticationEntryPoint {
//
//	@Throws(IOException::class, ServletException::class)
//	override fun commence(
//		request: HttpServletRequest,
//		response: HttpServletResponse,
//		exception: AuthenticationException
//	) {
//		val jsonBody: MutableMap<String, Any> = HashMap()
//		jsonBody["type"] = exception::class.java.simpleName
//		jsonBody["class"] = exception::class.java
//		jsonBody["message"] = exception.message.toString()
//		jsonBody["exception"] = exception.cause.toString()
//		jsonBody["path"] = request.servletPath
//		jsonBody["timestamp"] = Date().time
//		response.contentType = "application/json"
//		response.status = HttpServletResponse.SC_UNAUTHORIZED
//		val mapper = ObjectMapper()
//		mapper.writeValue(response.outputStream, jsonBody)
//	}
//}

@Component
class OAuth2FluxExceptionHandler : ServerAuthenticationEntryPoint {
	override fun commence(exchange: ServerWebExchange, exception: AuthenticationException): Mono<Void> {
		exchange.response.statusCode = HttpStatus.CONFLICT
		val bytes: ByteArray = "Some text".toByteArray(StandardCharsets.UTF_8)
		val buffer: DataBuffer = exchange.response.bufferFactory().wrap(bytes)

		return exchange.response.writeWith(Flux.just(buffer))
	}
}

// https://stackoverflow.com/questions/45211431/webexceptionhandler-how-to-write-a-body-with-spring-webflux
// https://newbedev.com/http-response-exception-handling-in-spring-5-reactive