package ru.nb.medalist.msmuser

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["ru.nb.medalist"])
class MsmUserApplication

fun main(args: Array<String>) {
	runApplication<MsmUserApplication>(*args)
}
