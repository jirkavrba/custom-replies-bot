package dev.vrba.customrepliesbot.discord

import dev.vrba.customrepliesbot.configuration.DiscordConfiguration
import dev.vrba.customrepliesbot.discord.commands.SlashCommand
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.core.env.Profiles
import org.springframework.stereotype.Component

@Component
class CommandsHandler(
        private val environment: Environment,
        private val configuration: DiscordConfiguration,
        private val commands: List<SlashCommand>
) : ListenerAdapter() {

    private val logger: Logger = LoggerFactory.getLogger(this::class.qualifiedName)

    override fun onReady(event: ReadyEvent) {

        val commands = commands.map { it.definition }

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

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        val name = event.name
        val handler = commands.firstOrNull { it.definition.name == name }
                ?: return logger.warn("Cannot find slash command handler for /$name")

        // TODO: Add proper exception handling
        handler.execute(event)
    }
}