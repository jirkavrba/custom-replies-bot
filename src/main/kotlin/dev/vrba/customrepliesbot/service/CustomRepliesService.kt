package dev.vrba.customrepliesbot.service

import dev.vrba.customrepliesbot.entities.CustomReply
import dev.vrba.customrepliesbot.repositories.CustomRepliesRepository
import org.springframework.stereotype.Service

@Service
class CustomRepliesService(private val repository: CustomRepliesRepository) {

    fun findMatchingReplies(message: String, guild: Long): List<CustomReply> {
        val replies = repository.findAllByGuildId(guild)
        val matching = replies.filter {
            // TODO: Maybe change this to a more sophisticated matching pattern
            message.contains(it.trigger)
        }

        return matching
    }

}