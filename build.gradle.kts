import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot")
	id("io.spring.dependency-management")
	kotlin("jvm")
	kotlin("plugin.spring")
}

group = "ru.nb.medalist"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
	maven { url = uri("https://artifactory-oss.prod.netflix.net/artifactory/maven-oss-candidates") }
}

extra["springCloudVersion"] = "2022.0.1"
extra["keycloakVersion"] = "20.0.3"

dependencies {

	implementation(project(":base"))

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.cloud:spring-cloud-starter-config")
	implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client") {
		exclude(group = "java.ws.rs", module = "jsr311-api") // for keycloak
	}
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
//	implementation("org.springframework.boot:spring-boot-starter-web")

//	implementation ("org.springframework.cloud:spring-cloud-starter-stream-rabbit:+")

//	implementation("org.keycloak:keycloak-admin-client:${property("keycloakVersion")}")
	implementation("org.keycloak:keycloak-admin-client:20.0.5")

//	testImplementation("org.springframework.amqp:spring-rabbit-test")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
		mavenBom("org.keycloak.bom:keycloak-adapter-bom:${property("keycloakVersion")}")
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
