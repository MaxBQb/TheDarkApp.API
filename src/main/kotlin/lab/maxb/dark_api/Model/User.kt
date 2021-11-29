package lab.maxb.dark_api.Model

import lab.maxb.dark_api.Model.getUUID
import java.util.*

open class User(
    open var name: String,
    open var rating: Int,
    open var id: UUID = getUUID(),
)