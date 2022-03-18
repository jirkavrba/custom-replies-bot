package dev.vrba.customrepliesbot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan(basePackages = ["dev.vrba.customrepliesbot.configuration"])
@SpringBootApplication
class CustomRepliesBotApplication

fun main(args: Array<String>) {
    runApplication<CustomRepliesBotApplication>(*args)
}
