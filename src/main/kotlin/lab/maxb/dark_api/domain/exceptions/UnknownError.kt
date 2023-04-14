package lab.maxb.dark_api.domain.exceptions

class UnknownError(details: String): DomainException(
    "Unknown error: $details"
)