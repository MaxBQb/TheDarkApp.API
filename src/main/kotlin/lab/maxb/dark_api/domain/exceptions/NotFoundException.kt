package lab.maxb.dark_api.domain.exceptions

class NotFoundException(
    message: String = "Not found"
) : DomainException(message) {
    companion object {
        fun of(details: String = "entity")
            = NotFoundException("$details not found")
    }
}
