package com.example.gff

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class GffApplication

fun main(args: Array<String>) {
    runApplication<GffApplication>(*args)
}
