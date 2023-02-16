@file:Suppress("unused")

package lab.maxb.dark_api.application.request

import lab.maxb.dark_api.domain.model.RecognitionTask
import lab.maxb.dark_api.domain.model.User
import java.util.*


class RecognitionTaskNetworkCreationDTO(
    var names: Set<String>
) {
    lateinit var owner: User
}

fun RecognitionTaskNetworkCreationDTO.toDomain() = RecognitionTask(
    names = names,
    owner = owner,
)