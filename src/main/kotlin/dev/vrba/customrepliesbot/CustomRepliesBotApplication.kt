package dev.vrba.customrepliesbot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@EnableCaching
@ConfigurationPropertiesScan(basePackages = ["dev.vrba.customrepliesbot.configuration"])
@SpringBootApplication
class CustomRepliesBotApplication

fun main(args: Array<String>) {
    runApplication<CustomRepliesBotApplication>(*args)
}
