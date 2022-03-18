package dev.vrba.customrepliesbot.discord.commands

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData
import net.dv8tion.jda.api.interactions.components.buttons.Button
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class ManageSlashCommand : SlashCommand {

    override val definition: SlashCommandData = Commands.slash("manage", "Display a management dashboard for the current guild")

    override fun execute(event: SlashCommandInteractionEvent) {
        val guild = event.guild ?: return event
                .reply("Sorry, this command can be only used inside guilds")
                .setEphemeral(true)
                .queue()

        val interaction = event.deferReply(true).complete()
        val member = guild.retrieveMember(event.user).complete()

        // TODO: Allow setting up a bot-specific management role
        if (!member.hasPermission(Permission.MANAGE_SERVER)) {
            return interaction
                    .editOriginal("Sorry, but you have to be an Administrator of this guild to use this command.")
                    .queue()
        }

        val embed = createDashboardEmbed(member)
        val buttons = listOf<Button>(
                Button.primary("manage:replies", "⭐ Manage replies"),
                Button.secondary("soon-tm", "More features coming soon").asDisabled()
        )

        interaction.editOriginalEmbeds(embed)
                .setActionRow(buttons)
                .queue()
    }

    private fun createDashboardEmbed(user: Member): MessageEmbed =
            EmbedBuilder()
                    .setColor(0x5865F2)
                    .setTitle("Management dashboard")
                    .setDescription("To configure this bot behaviour in this guild, please use the buttons below")
                    .setTimestamp(Instant.now())
                    .setAuthor(user.effectiveName, null, user.effectiveAvatarUrl)
                    .build()

}