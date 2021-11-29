package lab.maxb.dark_api.Model

import lab.maxb.dark_api.Model.Role
import lab.maxb.dark_api.Model.User
import lab.maxb.dark_api.Model.getUUID
import lab.maxb.dark_api.Model.toSHA256
import java.util.*


open class Profile(
    open var login: String,
    override var name: String,
    override var rating: Int,
    open var role: Role = Role.USER,
    override var id: UUID = getUUID(),
    open var hash: String? = null,
    password: String? = null,
): User(name, rating, id) {
    init {
        password?.let { hash = getHash(login, it) }
    }

    companion object {
        fun getHash(login: String, password: String)
            = (login + password).toSHA256()
    }
}