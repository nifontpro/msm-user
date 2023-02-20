package ru.nb.medalist.msmuser.keycloak

//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
//import org.springframework.security.config.annotation.web.builders.HttpSecurity
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
//import org.springframework.security.web.SecurityFilterChain
//
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
//			.requestMatchers("/admin/info").permitAll()
//			.requestMatchers("/auth/**").hasRole("user")
//			.requestMatchers("/admin/**").hasRole("admin")
//			.anyRequest().authenticated()
//
//			.and() // добавляем новые настройки, не связанные с предыдущими
//			.oauth2ResourceServer()// добавляем конвертер ролей из JWT в Authority (Role)
//			.jwt()
//			.jwtAuthenticationConverter(jwtAuthenticationConverter)
//
//		return http.build()
//
//	}
//}