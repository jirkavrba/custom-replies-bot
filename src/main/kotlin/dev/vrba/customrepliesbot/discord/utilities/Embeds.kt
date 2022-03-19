package dev.vrba.customrepliesbot.discord.utilities

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed

object Embeds {

    fun error(title: String, description: String? = null): MessageEmbed =
        EmbedBuilder()
            .setColor(0xED4245)
            .setTitle(title)
            .setDescription(description ?: "")
            .build()

}