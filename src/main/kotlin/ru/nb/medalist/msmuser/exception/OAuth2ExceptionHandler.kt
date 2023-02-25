package ru.nb.medalist.msmuser.exception

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap


// обрабатывает ошибки oauth2 (авторизация и пр.)
// web
@Suppress("unused")
class OAuth2ExceptionHandler : AuthenticationEntryPoint {

	@Throws(IOException::class, ServletException::class)
	override fun commence(
		request: HttpServletRequest,
		response: HttpServletResponse,
		exception: AuthenticationException
	) {
		val jsonBody: MutableMap<String, Any> = HashMap()
		jsonBody["type"] = exception::class.java.simpleName
		jsonBody["class"] = exception::class.java
		jsonBody["message"] = exception.message.toString()
		jsonBody["exception"] = exception.cause.toString()
		jsonBody["path"] = request.servletPath
		jsonBody["timestamp"] = Date().time
		response.contentType = "application/json"
		response.status = HttpServletResponse.SC_UNAUTHORIZED
		val mapper = ObjectMapper()
		mapper.writeValue(response.outputStream, jsonBody)
	}
}