package dev.vrba.customrepliesbot.repositories

import dev.vrba.customrepliesbot.entities.CustomReply
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CustomRepliesRepository : PagingAndSortingRepository<CustomReply, UUID>