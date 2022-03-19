package dev.vrba.customrepliesbot.repositories

import dev.vrba.customrepliesbot.entities.CustomReply
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CustomRepliesRepository : PagingAndSortingRepository<CustomReply, UUID> {

    @Cacheable("custom_replies")
    fun findAllByGuildId(guildId: Long): List<CustomReply>

    @Cacheable("custom_replies_page")
    fun findAllByGuildId(guildId: Long, page: Pageable): Page<CustomReply>

    @Cacheable("custom_replies_exists")
    fun existsByGuildIdAndName(guildId: Long, name: String): Boolean

    @Cacheable("custom_reply")
    fun findByGuildIdAndName(guild: Long, name: String): CustomReply?

    @Caching(
        evict = [
            CacheEvict("custom_replies", allEntries = true),
            CacheEvict("custom_replies_page", allEntries = true),
            CacheEvict("custom_replies_exists", allEntries = true),
            CacheEvict("custom_reply", allEntries = true)
        ]
    )
    override fun <S : CustomReply?> save(entity: S): S

    @Caching(evict = [
        CacheEvict("custom_replies", allEntries = true),
        CacheEvict("custom_replies_page", allEntries = true),
        CacheEvict("custom_replies_exists", allEntries = true),
        CacheEvict("custom_reply", allEntries = true)
    ])
    override fun delete(reply: CustomReply)
}