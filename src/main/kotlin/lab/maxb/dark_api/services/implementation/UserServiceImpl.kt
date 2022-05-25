package lab.maxb.dark_api.services.implementation

import lab.maxb.dark_api.model.User
import lab.maxb.dark_api.repository.dao.UserDAO
import lab.maxb.dark_api.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserServiceImpl @Autowired constructor(
    private val dataSource: UserDAO,
) : UserService {
    override fun get(id: UUID) = dataSource.findByIdEquals(id)
    override fun save(value: User) = dataSource.save(value)
    override fun addRating(id: UUID, value: Int) {
        get(id)?.let {
            it.rating += value
            save(it)
        }
    }
}