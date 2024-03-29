@file:Suppress("unused")

package lab.maxb.dark_api.application.request

import lab.maxb.dark_api.domain.model.RecognitionTask
import lab.maxb.dark_api.domain.model.User


class RecognitionTaskNetworkCreationDTO(
    var names: Set<String>,
    var images: List<String>,
)

fun RecognitionTaskNetworkCreationDTO.toDomain(owner: User) = RecognitionTask(
    names = names,
    images = images,
    owner = owner,
)