package pw.dotdash.township.plugin.event.town

import pw.dotdash.township.api.event.town.ChangeTownEvent
import pw.dotdash.township.api.resident.Resident
import pw.dotdash.township.api.town.Town
import org.spongepowered.api.event.Cancellable
import org.spongepowered.api.event.cause.Cause

abstract class ChangeTownEventImpl(
    private val town: Town,
    private val cause: Cause
) : ChangeTownEvent, Cancellable {

    private var cancelled: Boolean = false

    override fun isCancelled(): Boolean = this.cancelled

    override fun setCancelled(cancel: Boolean) {
        this.cancelled = cancel
    }

    override fun getTown(): Town = this.town

    override fun getCause(): Cause = this.cause

    class Name(
        private val oldName: String,
        private var newName: String,
        town: Town,
        cause: Cause
    ) : ChangeTownEventImpl(town, cause), ChangeTownEvent.Name {

        override fun getOldName(): String = this.oldName

        override fun getNewName(): String = this.newName

        override fun setNewName(name: String) {
            this.newName = name
        }
    }

    class Open(
        private var open: Boolean,
        town: Town,
        cause: Cause
    ) : ChangeTownEventImpl(town, cause), ChangeTownEvent.Open {

        override fun isOpen(): Boolean = this.open

        override fun setOpen(open: Boolean) {
            this.open = open
        }
    }

    class Owner(
        private val oldOwner: Resident,
        private var newOwner: Resident,
        town: Town,
        cause: Cause
    ) : ChangeTownEventImpl(town, cause), ChangeTownEvent.Owner {

        override fun getOldOwner(): Resident = this.oldOwner

        override fun getNewOwner(): Resident = this.newOwner

        override fun setNewOwner(resident: Resident) {
            this.newOwner = resident
        }
    }

    class Join(
        private val resident: Resident,
        town: Town,
        cause: Cause
    ) : ChangeTownEventImpl(town, cause), ChangeTownEvent.Join {

        override fun getResident(): Resident = this.resident
    }

    class Leave(
        private val resident: Resident,
        town: Town,
        cause: Cause
    ) : ChangeTownEventImpl(town, cause), ChangeTownEvent.Leave {

        override fun getResident(): Resident = this.resident
    }
}