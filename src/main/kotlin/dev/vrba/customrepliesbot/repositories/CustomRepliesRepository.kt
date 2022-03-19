package dev.vrba.customrepliesbot.repositories

import dev.vrba.customrepliesbot.entities.CustomReply
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
@Cacheable("custom_replies")
interface CustomRepliesRepository : PagingAndSortingRepository<CustomReply, UUID> {

    fun findAllByGuildId(guildId: Long): List<CustomReply>

    fun findAllByGuildId(guildId: Long, page: Pageable): Page<CustomReply>

    fun existsByGuildIdAndName(guildId: Long, name: String): Boolean

    fun findByGuildIdAndName(guild: Long, name: String): CustomReply?
}