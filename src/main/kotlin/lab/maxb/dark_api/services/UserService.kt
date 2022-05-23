package lab.maxb.dark_api.services
import lab.maxb.dark_api.model.User
import java.util.*

interface UserService {
    fun get(id: UUID): User?
    fun save(value: User): User
}
