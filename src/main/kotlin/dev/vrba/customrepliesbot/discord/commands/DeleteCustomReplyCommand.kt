package dev.vrba.customrepliesbot.discord.commands

import dev.vrba.customrepliesbot.repositories.CustomRepliesRepository
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class DeleteCustomReplyCommand(private val repository: CustomRepliesRepository) : SlashCommand {

    override val definition: SlashCommandData = Commands.slash("delete-custom-reply", "Delete the selected custom reply")
        .addOption(OptionType.STRING, "name", "Name of the custom reply that should be deleted", true)

    override fun execute(event: SlashCommandInteractionEvent) {
        val name = event.getOption("name")?.asString ?: throw IllegalArgumentException("Missing the name parameter")

        val interaction = event.deferReply().complete()
        val guild =  event.guild!!.idLong
        val reply = repository.findByGuildIdAndName(guild, name)

        if (reply == null) {
            val embed = EmbedBuilder()
                .setColor(0xED4245)
                .setTitle("There is no such custom reply with this name registered to this guild")
                .setTimestamp(Instant.now())
                .build()

            return interaction.editOriginalEmbeds(embed).queue()
        }

        repository.delete(reply)

        val embed = EmbedBuilder()
            .setColor(0x57F287)
            .setTitle("Custom reply deleted")
            .build()

        return interaction.editOriginalEmbeds(embed).queue()
    }
}