package dev.vrba.customrepliesbot.discord

import dev.vrba.customrepliesbot.configuration.DiscordConfiguration
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.build.Commands
import org.springframework.core.env.Environment
import org.springframework.core.env.Profiles
import org.springframework.stereotype.Component

@Component
class CommandsHandler(
        private val environment: Environment,
        private val configuration: DiscordConfiguration,
) : ListenerAdapter() {

    val commands = listOf(
        Commands.slash("manage", "Show interface for managing this guild's replies")
    )

    override fun onReady(event: ReadyEvent) {
        if (environment.acceptsProfiles(Profiles.of("development"))) {
            val id = configuration.developmentGuildId
            val guild = event.jda.getGuildById(id) ?: throw IllegalStateException("Cannot find the configured guild [$id]")

            return guild.updateCommands()
                    .addCommands(commands)
                    .queue()
        }

        return event.jda.updateCommands()
                .addCommands(commands)
                .queue()
    }
}