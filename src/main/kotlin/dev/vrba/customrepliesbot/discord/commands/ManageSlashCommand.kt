package dev.vrba.customrepliesbot.discord.commands

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData
import org.springframework.stereotype.Component

@Component
class ManageSlashCommand : SlashCommand {

    override val definition: SlashCommandData = Commands.slash("manage", "Display a management dashboard for the current guild")

    override fun execute(event: SlashCommandInteractionEvent) {
        event.deferReply(true)
    }

}