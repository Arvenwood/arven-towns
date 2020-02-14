package pw.dotdash.township.plugin.listener

import pw.dotdash.township.api.claim.Claim
import pw.dotdash.township.api.claim.ClaimService
import pw.dotdash.township.api.resident.Resident
import pw.dotdash.township.api.resident.ResidentService
import org.spongepowered.api.block.BlockSnapshot
import org.spongepowered.api.data.Transaction
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.block.ChangeBlockEvent
import org.spongepowered.api.event.block.tileentity.ChangeSignEvent
import org.spongepowered.api.event.filter.cause.Root
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.format.TextColors.RED
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World

class BlockListener {

    @Listener
    fun onBlockBreak(event: ChangeBlockEvent.Break, @Root player: Player) {
        checkProtection(event, player)
    }

    @Listener
    fun onBlockPlace(event: ChangeBlockEvent.Place, @Root player: Player) {
        checkProtection(event, player)
    }

    private fun checkProtection(event: ChangeBlockEvent, player: Player) {
        val resident: Resident = ResidentService.getInstance().getResident(player.uniqueId).orElse(null) ?: return

        for (transaction: Transaction<BlockSnapshot> in event.transactions) {
            val location: Location<World> = transaction.original.location.orElse(null) ?: continue
            val claim: Claim = ClaimService.getInstance().getClaimAt(location).orElse(null) ?: continue

            if (claim.town != resident.town) {
                event.isCancelled = true
                player.sendMessage(Text.of(RED, "That chunk is owned by the town ${claim.town.name}."))
                return
            }
        }
    }

    @Listener
    fun onChangeSign(event: ChangeSignEvent, @Root player: Player) {
        val resident: Resident = ResidentService.getInstance().getResident(player.uniqueId).orElse(null) ?: return
        val claim: Claim = ClaimService.getInstance().getClaimAt(event.targetTile.location).orElse(null) ?: return

        if (claim.town != resident.town) {
            event.isCancelled = true
            player.sendMessage(Text.of(RED, "That chunk is owned by the town ${claim.town.name}."))
            return
        }
    }
}