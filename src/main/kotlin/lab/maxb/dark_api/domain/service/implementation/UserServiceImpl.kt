package lab.maxb.dark_api.domain.service.implementation

import lab.maxb.dark_api.domain.gateway.UsersGateway
import lab.maxb.dark_api.domain.model.User
import lab.maxb.dark_api.domain.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserServiceImpl @Autowired constructor(
    private val usersGateway: UsersGateway,
) : UserService {
    override fun getUser(id: UUID) = usersGateway.findById(id)
    override fun addUser(value: User) = usersGateway.save(value)
    override fun addRating(id: UUID, value: Int) {
        getUser(id)?.let {
            addUser(it.copy(rating = it.rating + value))
        }
    }
}