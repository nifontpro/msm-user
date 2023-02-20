package ru.nb.medalist.msmuser.keycloak

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import reactor.core.publisher.Mono


// https://www.baeldung.com/spring-security-5-reactive

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class SpringSecurityFluxConfig {

	@Bean
	fun springSecurityFilterChain(
		http: ServerHttpSecurity,
		jwtAuthenticationConverter: Converter<Jwt, Mono<AbstractAuthenticationToken>>
	): SecurityWebFilterChain {

		http.authorizeExchange()
			.pathMatchers("/admin/info").permitAll()
			.pathMatchers("/user/**").hasRole("user")
			.pathMatchers("/admin/**").hasRole("admin")
			.anyExchange().authenticated()
			.and()
			.csrf().disable()
			.cors() // Разрешает запросы типа OPTIONS
			.and()
			.oauth2ResourceServer()
			.jwt()
			.jwtAuthenticationConverter(jwtAuthenticationConverter)

		return http.build()
	}

	@Bean
	fun corsConfigurationSource(): CorsConfigurationSource? {
		val configuration = CorsConfiguration()
		configuration.allowedOrigins = listOf("*")
		configuration.allowedHeaders = listOf("*")
		configuration.allowedMethods = listOf("*")
		val source = UrlBasedCorsConfigurationSource()
		source.registerCorsConfiguration("/**", configuration)
		return source
	}

}