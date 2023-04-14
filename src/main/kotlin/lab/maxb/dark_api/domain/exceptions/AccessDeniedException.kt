package lab.maxb.dark_api.domain.exceptions

class AccessDeniedException(
    message: String = "Operation not allowed for current user or role"
): DomainException(message)