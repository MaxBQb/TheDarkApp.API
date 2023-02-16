package lab.maxb.dark_api.domain.gateway

import lab.maxb.dark_api.domain.model.User
import java.util.*

interface UsersGateway : ReadWriteGateway<UUID, User>