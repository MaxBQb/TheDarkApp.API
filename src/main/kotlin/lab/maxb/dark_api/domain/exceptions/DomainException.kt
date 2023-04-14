package lab.maxb.dark_api.domain.exceptions

abstract class DomainException(
    override val message: String
): RuntimeException(message)