package pw.dotdash.township.plugin.command

import pw.dotdash.township.api.claim.Claim
import pw.dotdash.township.api.claim.ClaimService
import pw.dotdash.township.api.resident.Resident
import pw.dotdash.township.api.resident.ResidentService
import pw.dotdash.township.api.town.Town
import org.spongepowered.api.command.CommandException
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.command.args.CommandContext
import org.spongepowered.api.command.spec.CommandExecutor
import org.spongepowered.api.command.spec.CommandSpec
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.format.TextColors.GREEN

object CommandTownClaim : CommandExecutor {

    val SPEC: CommandSpec = CommandSpec.builder()
        .permission("township.town.claim.base")
        .executor(this)
        .build()

    override fun execute(src: CommandSource, args: CommandContext): CommandResult {
        if (src !is Player) throw CommandException(Text.of("You must be a player to use that command."))

        val resident: Resident = ResidentService.getInstance().getOrCreateResident(src)

        val town: Town = resident.town.orElse(null)
            ?: throw CommandException(Text.of("You must be in a town to use that command."))

        ClaimService.getInstance().getClaimAt(src.location).orElse(null)?.let {
            throw CommandException(Text.of("This chunk has already been claimed by ${it.town.name}."))
        }

        val claim: Claim = Claim.builder()
            .world(src.world)
            .chunkPosition(src.location.chunkPosition)
            .town(town)
            .build()

        ClaimService.getInstance().register(claim)
        src.sendMessage(Text.of(GREEN, "Claimed the chunk at ${claim.chunkPosition}"))

        return CommandResult.success()
    }
}