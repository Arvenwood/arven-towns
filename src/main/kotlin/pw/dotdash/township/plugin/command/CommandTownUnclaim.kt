package pw.dotdash.township.plugin.command

import org.spongepowered.api.command.CommandException
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.format.TextColors.GREEN
import pw.dotdash.director.core.HNil
import pw.dotdash.township.api.claim.Claim
import pw.dotdash.township.api.claim.ClaimService
import pw.dotdash.township.api.resident.Resident
import pw.dotdash.township.api.resident.ResidentService
import pw.dotdash.township.api.town.Town
import pw.dotdash.township.plugin.util.unwrap

object CommandTownUnclaim {

    fun unclaim(src: CommandSource, args: HNil): CommandResult {
        if (src !is Player) throw CommandException(Text.of("You must be a player to use that command."))

        val resident: Resident = ResidentService.getInstance().getOrCreateResident(src)

        val town: Town = resident.town.unwrap()
            ?: throw CommandException(Text.of("You must be in a town to use that command."))

        val claim: Claim = ClaimService.getInstance().getClaimAt(src.location).unwrap()
            ?: throw CommandException(Text.of("This chunk has not been claimed."))

        if (claim.town != town) {
            throw CommandException(Text.of("Your town does not own this chunk."))
        }

        ClaimService.getInstance().unregister(claim)
        src.sendMessage(Text.of(GREEN, "Unclaimed the chunk at ${claim.chunkPosition}"))

        return CommandResult.success()
    }
}