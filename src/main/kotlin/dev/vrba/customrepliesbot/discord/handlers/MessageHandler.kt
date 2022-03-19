package dev.vrba.customrepliesbot.discord.handlers

import dev.vrba.customrepliesbot.services.CustomRepliesService
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.utils.AllowedMentions
import org.springframework.stereotype.Component

@Component
class MessageHandler(private val service: CustomRepliesService): ListenerAdapter() {

    private val mention = "{mention}"

    override fun onMessageReceived(event: MessageReceivedEvent) {
        // Ignore messages from the bot itself to prevent infinite loops
        if (event.author.idLong == event.jda.selfUser.idLong) return

        val guild = event.guild.idLong
        val replies = service.findMatchingReplies(event.message.contentRaw, guild)

        replies.forEach {
            if (it.image != null) {
                val embed = EmbedBuilder()
                    .setTitle(it.response)
                    .setImage(it.image)
                    .build()

                return@forEach event.message
                    .replyEmbeds(embed)
                    .queue()
            }

            val content = it.response.replace(mention, event.message.author.asMention)

            event.message.reply(content).queue()
        }
    }
}