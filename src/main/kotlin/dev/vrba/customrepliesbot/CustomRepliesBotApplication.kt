package dev.vrba.customrepliesbot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CustomRepliesBotApplication

fun main(args: Array<String>) {
    runApplication<CustomRepliesBotApplication>(*args)
}
