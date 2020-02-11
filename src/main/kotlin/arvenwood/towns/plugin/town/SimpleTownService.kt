package arvenwood.towns.plugin.town

import arvenwood.towns.api.claim.Claim
import arvenwood.towns.api.claim.ClaimService
import arvenwood.towns.api.resident.Resident
import arvenwood.towns.api.town.Town
import arvenwood.towns.api.town.TownService
import arvenwood.towns.plugin.event.town.CreateTownEventImpl
import arvenwood.towns.plugin.event.town.DeleteTownEventImpl
import org.spongepowered.api.Sponge
import java.util.*

class SimpleTownService : TownService {

    private val townsById = HashMap<UUID, Town>()
    private val townsByName = HashMap<String, Town>()

    override fun getTowns(): Collection<Town> =
        this.townsById.values.toSet()

    override fun getTown(uniqueId: UUID): Optional<Town> =
        Optional.ofNullable(this.townsById[uniqueId])

    override fun getTown(name: String): Optional<Town> =
        Optional.ofNullable(this.townsByName[name])

    override fun register(town: Town): Boolean {
        if (town.uniqueId in this.townsById) {
            return false
        }

        val event = CreateTownEventImpl(town, Sponge.getCauseStackManager().currentCause)
        Sponge.getEventManager().post(event)
        if (event.isCancelled) {
            return false
        }

        this.townsById[town.uniqueId] = town
        this.townsByName[town.name] = town
        return true
    }

    override fun unregister(town: Town): Boolean {
        if (town.uniqueId !in this.townsById) {
            return false
        }

        val event = DeleteTownEventImpl(town, Sponge.getCauseStackManager().currentCause)
        Sponge.getEventManager().post(event)
        if (event.isCancelled) {
            return false
        }

        this.townsById.remove(town.uniqueId)
        this.townsByName.remove(town.name)

        for (resident: Resident in town.residents) {
            town.removeResident(resident)
        }

        for (claim: Claim in town.claims) {
            ClaimService.get().unregister(claim)
        }

        return true
    }
}