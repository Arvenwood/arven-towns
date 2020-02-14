package pw.dotdash.township.api.claim;

import com.flowpowered.math.vector.Vector3i;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.util.ResettableBuilder;
import org.spongepowered.api.util.Tristate;
import org.spongepowered.api.world.World;
import pw.dotdash.township.api.permission.ClaimPermission;
import pw.dotdash.township.api.role.Role;
import pw.dotdash.township.api.town.Town;
import pw.dotdash.township.api.town.TownService;

import java.util.Map;
import java.util.UUID;

public interface Claim extends DataSerializable {

    static Builder builder() {
        return Sponge.getRegistry().createBuilder(Builder.class);
    }

    UUID getWorldUniqueId();

    default World getWorld() {
        return Sponge.getServer()
                .getWorld(this.getWorldUniqueId())
                .orElseThrow(() -> new IllegalStateException("World " + this.getWorldUniqueId() + " is not loaded"));
    }

    Vector3i getChunkPosition();

    UUID getTownUniqueId();

    default Town getTown() {
        return TownService.getInstance()
                .getTown(this.getTownUniqueId())
                .orElseThrow(() -> new IllegalStateException("Town " + this.getTownUniqueId() + " is not loaded"));
    }

    Map<ClaimPermission, Boolean> getPermissionOverrides(Role role);

    Tristate getPermissionOverride(Role role, ClaimPermission permission);

    boolean setPermissionOverride(Role role, ClaimPermission permission, boolean value);

    boolean removePermissionOverride(Role role, ClaimPermission permission);

    interface Builder extends ResettableBuilder<Claim, Builder> {

        Builder world(World world);

        Builder chunkPosition(Vector3i chunkPosition);

        Builder town(Town town);

        Builder addPermissionOverride(Role role, ClaimPermission permission, boolean value);

        Claim build();
    }
}