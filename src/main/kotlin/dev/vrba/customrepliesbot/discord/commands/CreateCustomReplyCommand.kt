package dev.vrba.customrepliesbot.discord.commands

import dev.vrba.customrepliesbot.services.CustomRepliesService
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData
import org.springframework.stereotype.Component

@Component
class CreateCustomReplyCommand(private val service: CustomRepliesService) : SlashCommand {

    override val definition: SlashCommandData = Commands.slash("create-custom-reply", "Create a new custom reply mapping for this guild")
        .addOption(OptionType.STRING, "name", "Name (used for managing replies)", true)
        .addOption(OptionType.STRING, "trigger", "Word / phrase that triggers this reply", true)
        .addOption(OptionType.STRING, "response", "Text that should be responded with", true)

    override fun execute(event: SlashCommandInteractionEvent) {
        val name = event.getOption("name")?.asString ?: throw IllegalArgumentException("Missing the name parameter")
        val trigger = event.getOption("trigger")?.asString ?: throw IllegalArgumentException("Missing the trigger parameter")
        val response = event.getOption("response")?.asString ?: throw IllegalArgumentException("Missing the response parameter")

        if (!event.isFromGuild) {
            return event.reply("Sorry, this command can be only used inside guilds").queue()
        }

        if (!event.member!!.hasPermission(Permission.MANAGE_SERVER)) {
            return event.reply("Sorry, this command can be only used by administrators of this guild").queue()
        }

        val interaction = event.deferReply().complete()

        val guild =  event.guild?.idLong ?: 0
        val reply = service.createCustomReply(name, trigger, response, guild)
        val embed = EmbedBuilder()
            .setColor(0x57F287)
            .setTitle("Custom reply created")
            .addField("Name", name, false)
            .addField("Trigger", trigger, false)
            .addField("Response", response, false)
            .setFooter(reply.id.toString())
            .build()

        interaction.editOriginalEmbeds(embed).queue()
    }

}