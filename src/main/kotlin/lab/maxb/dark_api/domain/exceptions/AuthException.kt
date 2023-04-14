package lab.maxb.dark_api.domain.exceptions

sealed class AuthException(message: String) : DomainException(message) {
    class WrongCredentials: AuthException("Incorrect login or password")
    class AlreadyExists: AuthException("This login is already taken")
}