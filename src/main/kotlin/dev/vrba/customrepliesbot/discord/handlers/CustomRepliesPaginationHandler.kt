package dev.vrba.customrepliesbot.discord.handlers

import dev.vrba.customrepliesbot.discord.utilities.Embeds
import dev.vrba.customrepliesbot.repositories.CustomRepliesRepository
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.components.buttons.Button
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component

@Component
class CustomRepliesPaginationHandler(private val repository: CustomRepliesRepository) : ListenerAdapter() {

    override fun onButtonInteraction(event: ButtonInteractionEvent) {
        // Only handle the reply:x interactions
        if (event.componentId?.startsWith("reply:") != true) return

        val number = event.componentId.removePrefix("reply:").toInt()
        val guild = event.guild?.idLong ?: return
        val page = repository.findAllByGuildId(guild, PageRequest.of(number, 1))

        // Indicates an out-of-sync embed
        if (!page.hasContent()) {
            return event.message.delete().queue()
        }

        // Acknowledge this event
        event.interaction.deferEdit().queue()

        val embed = Embeds.customRepliesPageEmbed(page)
        val components = listOf(
            Button.secondary("reply:${page.number - 1}", "Previous").withDisabled(!page.hasPrevious()),
            Button.secondary("reply:${page.number + 1}", "Next").withDisabled(!page.hasNext())
        )

        event.message.editMessageEmbeds(embed)
            .setActionRow(components)
            .queue()
    }

}