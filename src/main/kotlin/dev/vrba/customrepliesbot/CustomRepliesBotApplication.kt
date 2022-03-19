package dev.vrba.customrepliesbot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
@ConfigurationPropertiesScan(basePackages = ["dev.vrba.customrepliesbot.configuration"])
class CustomRepliesBotApplication

fun main(args: Array<String>) {
    runApplication<CustomRepliesBotApplication>(*args)
}
