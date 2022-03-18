package dev.vrba.customrepliesbot.entities

import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
@Table(
        name = "custom_replies",
        uniqueConstraints = [
            UniqueConstraint(name = "UK_name_guild_id", columnNames = ["name", "guild_id"])
        ]
)
data class CustomReply(
    @Id
    var id: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val trigger: String,

    @Column(nullable = false)
    val response: String,

    @Column(name = "guild_id", nullable = false)
    val guildId: Long
)