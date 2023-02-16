package lab.maxb.dark_api.domain.service
import lab.maxb.dark_api.domain.model.User
import java.util.*

interface UserService {
    fun getUser(id: UUID): User?
    fun addUser(value: User): User
    fun addRating(id: UUID, value: Int = 1)
}
