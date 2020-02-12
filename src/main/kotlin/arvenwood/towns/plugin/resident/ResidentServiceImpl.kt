package arvenwood.towns.plugin.resident

import arvenwood.towns.api.resident.Resident
import arvenwood.towns.api.resident.ResidentService
import org.spongepowered.api.entity.living.player.Player
import java.util.*
import kotlin.collections.HashMap

class ResidentServiceImpl : ResidentService {

    private val residentsById = HashMap<UUID, Resident>()
    private val residentsByName = HashMap<String, Resident>()

    private val systemResident = ResidentImpl(UUID(0, 0), "TOWNS-SYS", null)

    override fun getSystemResident(): Resident =
        this.systemResident

    override fun getResidents(): Collection<Resident> =
        this.residentsById.values.toSet()

    override fun getResident(uniqueId: UUID): Optional<Resident> =
        Optional.ofNullable(this.residentsById[uniqueId])

    override fun getResident(name: String): Optional<Resident> =
        Optional.ofNullable(this.residentsByName[name])

    override fun getOrCreateResident(player: Player): Resident {
        this.getResident(player.uniqueId).orElse(null)?.let { return it }

        val resident = ResidentImpl(
            uniqueId = player.uniqueId,
            name = player.name,
            town = null
        )

        this.residentsById[resident.uniqueId] = resident
        this.residentsByName[resident.name] = resident

        return resident
    }
}