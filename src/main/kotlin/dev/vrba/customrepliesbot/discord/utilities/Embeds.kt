package dev.vrba.customrepliesbot.discord.utilities

import dev.vrba.customrepliesbot.entities.CustomReply
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import org.springframework.data.domain.Page

object Embeds {

    fun error(title: String, description: String? = null): MessageEmbed =
        EmbedBuilder()
            .setColor(0xED4245)
            .setTitle(title)
            .setDescription(description ?: "")
            .build()

    fun customReplyEmbed(reply: CustomReply, title: String): MessageEmbed =
        EmbedBuilder()
            .setColor(0x5865F2)
            .setTitle(title)
            .addField("Name", reply.name, false)
            .addField("Trigger", reply.trigger, false)
            .addField("Response", reply.response, false)
            .addField("Image", reply.image ?: "_No embed image_", false)
            .setThumbnail(reply.image)
            .setFooter(reply.id.toString())
            .build()

    fun customRepliesCompactEmbed(replies: List<CustomReply>): MessageEmbed =
        EmbedBuilder()
            .setColor(0x5865F2)
            .setTitle("Custom replies registered for this guild")
            .setDescription(replies.joinToString("\n\n") {
                """
                **${it.name}**:
                ${it.trigger} -> ${it.response}
                """.trimIndent()
            })
            .build()

    fun customRepliesPageEmbed(page: Page<CustomReply>): MessageEmbed {
        val reply = page.content.first()
        val title = "Custom reply ${page.number + 1} / ${page.totalPages}"

        return customReplyEmbed(reply, title)
    }

}