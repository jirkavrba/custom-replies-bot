package dev.vrba.customrepliesbot.discord.commands

import dev.vrba.customrepliesbot.entities.CustomReply
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
class CreateCustomReplyCommand(private val repository: CustomRepliesRepository) : SlashCommand {

    override val definition: SlashCommandData = Commands.slash("create-custom-reply", "Create a new custom reply mapping for this guild")
        .addOption(OptionType.STRING, "name", "Name (used for managing replies)", true)
        .addOption(OptionType.STRING, "trigger", "Word / phrase that triggers this reply", true)
        .addOption(OptionType.STRING, "response", "Text that should be responded with", true)
        .addOption(OptionType.STRING, "image", "Image url that should be displayed inside an embed")

    override fun execute(event: SlashCommandInteractionEvent) {
        val name = event.getOption("name")?.asString ?: throw IllegalArgumentException("Missing the name parameter")
        val trigger = event.getOption("trigger")?.asString ?: throw IllegalArgumentException("Missing the trigger parameter")
        val response = event.getOption("response")?.asString ?: throw IllegalArgumentException("Missing the response parameter")
        val image = event.getOption("image")?.asString

        if (!event.isFromGuild) {
            return event.reply("Sorry, this command can be only used inside guilds").queue()
        }

        if (!event.member!!.hasPermission(Permission.MANAGE_SERVER)) {
            return event.reply("Sorry, this command can be only used by administrators of this guild").queue()
        }

        val interaction = event.deferReply().complete()
        val guild =  event.guild!!.idLong

        if (repository.existsByGuildIdAndName(guild, name)) {
            val embed = EmbedBuilder()
                .setColor(0xED4245)
                .setTitle("There is already a custom reply with this name registered to this guild")
                .setTimestamp(Instant.now())
                .build()

            return interaction.editOriginalEmbeds(embed).queue()
        }

        val entity = CustomReply(name = name, trigger = trigger, response = response, guildId = guild, image = image)
        val reply = repository.save(entity)
        val embed = EmbedBuilder()
            .setColor(0x57F287)
            .setTitle("Custom reply created")
            .addField("Name", name, false)
            .addField("Trigger", trigger, false)
            .addField("Response", response, false)
            .addField("Image", image ?: "_No embed image_", false)
            .setFooter(reply.id.toString())
            .build()

        interaction.editOriginalEmbeds(embed).queue()
    }

}