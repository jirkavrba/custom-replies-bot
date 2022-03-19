package dev.vrba.customrepliesbot.discord.handlers

import dev.vrba.customrepliesbot.services.CustomRepliesService
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.stereotype.Component

@Component
class MessageHandler(private val service: CustomRepliesService): ListenerAdapter() {

    override fun onMessageReceived(event: MessageReceivedEvent) {
        val guild = event.guild?.idLong ?: return
        val replies = service.findMatchingReplies(event.message.contentRaw, guild)

        replies.forEach { event.message.reply(it.response).queue() }
    }

}