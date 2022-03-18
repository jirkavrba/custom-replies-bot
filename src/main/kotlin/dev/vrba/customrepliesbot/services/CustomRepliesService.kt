package dev.vrba.customrepliesbot.services

import dev.vrba.customrepliesbot.entities.CustomReply
import dev.vrba.customrepliesbot.repositories.CustomRepliesRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
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

    @Suppress("NAME_SHADOWING")
    fun getCustomRepliesPage(guild: Long, page: Int): Page<CustomReply> =
            repository.findAllByGuildId(guild, PageRequest.of(page, 1))
}