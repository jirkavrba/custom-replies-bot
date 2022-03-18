package dev.vrba.customrepliesbot.discord

import dev.vrba.customrepliesbot.configuration.DiscordConfiguration
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.hooks.EventListener
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Service

@Service
class DiscordBotService(
        private val configuration: DiscordConfiguration,
        private val listeners: List<EventListener>
) : CommandLineRunner, ListenerAdapter() {

    override fun run(vararg args: String) {
        JDABuilder.createDefault(configuration.token)
                .addEventListeners(this)
                .addEventListeners(*listeners.toTypedArray())
                .build()
                .awaitReady()
    }

    override fun onReady(event: ReadyEvent) {
        event.jda.presence.activity = Activity.playing("with your patience")
    }
}