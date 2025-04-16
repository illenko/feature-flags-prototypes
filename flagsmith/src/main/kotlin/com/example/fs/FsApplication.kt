package com.example.fs

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FsApplication

fun main(args: Array<String>) {
	runApplication<FsApplication>(*args)
}
