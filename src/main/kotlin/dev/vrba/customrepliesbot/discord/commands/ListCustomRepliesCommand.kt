package dev.vrba.customrepliesbot.discord.commands

import dev.vrba.customrepliesbot.discord.utilities.Embeds
import dev.vrba.customrepliesbot.repositories.CustomRepliesRepository
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData
import net.dv8tion.jda.api.interactions.components.buttons.Button
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component

@Component
class ListCustomRepliesCommand(private val repository: CustomRepliesRepository) : SlashCommand {

    override val definition: SlashCommandData = Commands.slash("list-custom-replies", "List custom replies for this guild")
        .addOption(OptionType.BOOLEAN, "compact", "Defaults to false (1 reply per embed)", false)

    override fun execute(event: SlashCommandInteractionEvent) {
        val interaction = event.deferReply().complete()
        val guild = event.guild?.idLong ?: return

        val compact = event.getOption("compact")?.asBoolean ?: false

        if (compact) {
            val replies = repository.findAllByGuildId(guild)
            val embed = Embeds.customRepliesCompactEmbed(replies)

            return interaction.editOriginalEmbeds(embed).queue()
        }

        val page = repository.findAllByGuildId(guild, PageRequest.of(0, 1))

        if (!page.hasContent()) {
            val embed = EmbedBuilder()
                .setColor(0xED4245)
                .setTitle("There are no custom replies registered for this guild")
                .setDescription("To setup a new custom reply, please use the `/create-custom-reply` command")
                .build()

            return interaction.editOriginalEmbeds(embed).queue()
        }

        val embed = Embeds.customRepliesPageEmbed(page)
        val components = listOf(
            Button.secondary("reply:0", "Previous").withDisabled(!page.hasPrevious()),
            Button.secondary("reply:1", "Next").withDisabled(!page.hasNext())
        )

        interaction.editOriginalEmbeds(embed)
            .setActionRow(components)
            .queue()
    }
}