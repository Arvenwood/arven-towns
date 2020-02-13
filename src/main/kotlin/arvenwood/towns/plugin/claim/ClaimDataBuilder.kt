package arvenwood.towns.plugin.claim

import arvenwood.towns.api.claim.Claim
import arvenwood.towns.plugin.storage.DataQueries
import arvenwood.towns.plugin.util.getUUID
import com.flowpowered.math.vector.Vector3i
import org.spongepowered.api.data.DataView
import org.spongepowered.api.data.persistence.AbstractDataBuilder
import org.spongepowered.api.data.persistence.DataBuilder
import java.util.*

class ClaimDataBuilder : AbstractDataBuilder<Claim>(Claim::class.java, 1) {

    override fun buildContent(container: DataView): Optional<Claim> {
        val worldUniqueId: UUID = container.getUUID(DataQueries.WORLD_UNIQUE_ID).get()
        val chunkPosition = Vector3i(
            container.getInt(DataQueries.CHUNK_POSITION_X).get(),
            container.getInt(DataQueries.CHUNK_POSITION_Y).get(),
            container.getInt(DataQueries.CHUNK_POSITION_Z).get()
        )
        val townUniqueId: UUID = container.getUUID(DataQueries.TOWN_UNIQUE_ID).get()

        val claim = ClaimImpl(
            worldUniqueId = worldUniqueId,
            chunkPosition = chunkPosition,
            townUniqueId = townUniqueId
        )

        return Optional.of(claim)
    }
}