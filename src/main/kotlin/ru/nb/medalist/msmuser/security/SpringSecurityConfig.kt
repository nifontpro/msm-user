package ru.nb.medalist.msmuser.security
//
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
//import org.springframework.security.config.annotation.web.builders.HttpSecurity
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
//import org.springframework.security.web.SecurityFilterChain
//import org.springframework.web.cors.CorsConfiguration
//import org.springframework.web.cors.CorsConfigurationSource
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource
//import ru.nb.medalist.msmuser.exception.OAuth2ExceptionHandler
//
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity
//class SpringSecurityConfig {
//
//	@Bean
//	fun filterChain(http: HttpSecurity): SecurityFilterChain {
//
//		// конвертер для настройки spring security
//		val jwtAuthenticationConverter = JwtAuthenticationConverter()
//		// подключаем конвертер ролей
//		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(KCRoleConverter())
//
//		http.authorizeHttpRequests()
//			.requestMatchers("/admin/test").permitAll()
//			.requestMatchers("/auth/**").hasRole("user")
//			.requestMatchers("/admin/**").hasRole("admin")
//			.anyRequest().authenticated()
//			.and() // добавляем новые настройки, не связанные с предыдущими
//			.csrf().disable()
////			.cors()// Разрешает запросы типа OPTIONS
////			.and()
//			.oauth2ResourceServer()// добавляем конвертер ролей из JWT в Authority (Role)
//			.jwt()
//			.jwtAuthenticationConverter(jwtAuthenticationConverter)
//			.and()
//			.authenticationEntryPoint(OAuth2ExceptionHandler())
//
//		return http.build()
//	}
//
////	@Bean
////	fun corsConfigurationSource(): CorsConfigurationSource {
////		val configuration = CorsConfiguration() // .applyPermitDefaultValues()
////			.apply {
////				allowedOrigins = listOf("*")
////				allowedHeaders = listOf("*")
////				allowedMethods = listOf("*")
////			}
////		val source = UrlBasedCorsConfigurationSource().apply {
////			registerCorsConfiguration("/**", configuration)
////		}
////		return source
////	}
//}