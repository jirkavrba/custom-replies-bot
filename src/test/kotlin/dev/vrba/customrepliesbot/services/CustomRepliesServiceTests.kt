package dev.vrba.customrepliesbot.services

import dev.vrba.customrepliesbot.entities.CustomReply
import dev.vrba.customrepliesbot.repositories.CustomRepliesRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class CustomRepliesServiceTests {

    @Test
    fun `test matching guilds`() {
        val first = CustomReply(name = "test1", trigger = "test", response = "Ha! Test!", guildId = 1L)
        val second = CustomReply(name = "test2", trigger = "test", response = "Ha! Test!", guildId = 1L)
        val third = CustomReply(name = "test3", trigger = "test", response = "Ha! Test!", guildId = 2L)
        val forth = CustomReply(name = "test4", trigger = "test", response = "Ha! Test!", guildId = 3L)

        val repository: CustomRepliesRepository = mockk()

        every { repository.findAllByGuildId(0L) }.returns(emptyList())
        every { repository.findAllByGuildId(1L) }.returns(listOf(first, second))
        every { repository.findAllByGuildId(2L) }.returns(listOf(third))
        every { repository.findAllByGuildId(3L) }.returns(listOf(forth))

        val service = CustomRepliesService(repository)
        val message = "Tohle je test!"

        assertTrue(service.findMatchingReplies(message, 0L).isEmpty())
        assertTrue(service.findMatchingReplies(message, 1L).isNotEmpty())
        assertTrue(service.findMatchingReplies(message, 2L).isNotEmpty())
        assertTrue(service.findMatchingReplies(message, 3L).isNotEmpty())

        assertTrue(service.findMatchingReplies(message, 1L).all { it.guildId == 1L })
        assertTrue(service.findMatchingReplies(message, 2L).all { it.guildId == 2L })
        assertTrue(service.findMatchingReplies(message, 3L).all { it.guildId == 3L })
    }

    fun `test matching messages`() {
        val first = CustomReply(name = "test1", trigger = "test", response = "lmao", guildId = 1L)
        val second = CustomReply(name = "test2", trigger = "not test", response = "kekw", guildId = 1L)
        val third = CustomReply(name = "test3", trigger = "not", response = "xd", guildId = 1L)

        val repository: CustomRepliesRepository = mockk()

        every { repository.findAllByGuildId(1L) }.returns(listOf(first, second, third))

        val service = CustomRepliesService(repository)
        val guild = 1L

        assertEquals(1, service.findMatchingReplies("sample test", guild).size)
        assertEquals("test1", service.findMatchingReplies("sample test", guild).first().name)

        assertEquals(2, service.findMatchingReplies("not test", guild).size)
        assertTrue(service.findMatchingReplies("not test", guild).any { it.name == "test1" })
        assertTrue(service.findMatchingReplies("not test", guild).any { it.name == "test2" })

        assertEquals(1, service.findMatchingReplies("not so sample test", guild).size)
        assertTrue(service.findMatchingReplies("not so sample test", guild).all { it.name == "test3" })
    }
}