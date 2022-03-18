package dev.vrba.customrepliesbot.discord.interactions

import dev.vrba.customrepliesbot.entities.CustomReply
import dev.vrba.customrepliesbot.services.CustomRepliesService
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.components.buttons.Button
import org.springframework.data.domain.Page
import org.springframework.stereotype.Component

@Component
class ManageRepliesInteraction(private val service: CustomRepliesService) : ListenerAdapter() {

    override fun onButtonInteraction(event: ButtonInteractionEvent) {
        val id = event.componentId

        // Only handle the interaction if it belongs to the replies module
        if (!id.startsWith("manage:replies")) return

        when {
            id == "manage:replies" -> showRepliesEmbed(event)
            id.startsWith("manage:replies:") -> showRepliesEmbed(event, id.removePrefix("manage:replies:").toInt())
        }
    }

    @Suppress("NAME_SHADOWING")
    private fun showRepliesEmbed(event: ButtonInteractionEvent, page: Int = 1) {
        val interaction = event.deferReply(true).complete()

        val page = service.getCustomRepliesPage(event.guild!!.idLong, page)
        val components = listOf(
                Button.primary("manage:reply:add", "✨ Add a new custom reply"),
                Button.secondary("manage:reply:${page.content.firstOrNull()?.id ?: 0}", "\uD83D\uDD27 Edit this reply").withDisabled(!page.hasContent()),
                Button.secondary("manage:replies:${page.number - 1}", "\uD83D\uDC48 Previous").withDisabled(page.hasPrevious()),
                Button.secondary("manage:replies:${page.number + 1}", "\uD83D\uDC49 Next").withDisabled(page.hasPrevious()),
        )

        interaction.editOriginalEmbeds(replyEmbed(page))
                .setActionRow(components)
                .queue()
    }

    private fun replyEmbed(page: Page<CustomReply>): MessageEmbed {
        if (!page.hasContent()) {
            return EmbedBuilder()
                    .setTitle("There are no custom replies yet")
                    .setDescription("To add a custom reply, please use the button below")
                    .build()
        }

        val reply = page.content.first()

        return EmbedBuilder()
                .setTitle("Custom reply ${page.number} / ${page.totalPages}")
                .addField("ID", "`${reply.id}`", false)
                .addField("Name", reply.name, false)
                .addField("Trigger", reply.trigger, false)
                .addField("Response", reply.response, false)
                .build()
    }
}