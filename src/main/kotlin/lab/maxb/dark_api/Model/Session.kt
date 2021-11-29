package lab.maxb.dark_api.Model

import lab.maxb.dark_api.Model.getTimestampNow
import lab.maxb.dark_api.Model.getUUID
import lab.maxb.dark_api.Model.toSHA256
import java.util.*

open class Session(
    open var profile: Profile?,
    open var expires: Long = getTimestampNow() + SESSION_MAX_DURATION,
    open var hash: String? = null,
    open var auth_code: String? = null,
    open var id: UUID = getUUID(),
) {
    init {
        profile?.hash?.let { generateHash(it) }
    }

    private fun generateHash(passwordHash: String)  {
        hash = (id.toString() + expires + passwordHash).toSHA256()
    }

    fun prolong(passwordHash: String) {
        expires += SESSION_MAX_DURATION
        generateHash(passwordHash)
    }

    companion object {
        const val SESSION_MAX_DURATION = 10*24*60*60*1000
    }
}
