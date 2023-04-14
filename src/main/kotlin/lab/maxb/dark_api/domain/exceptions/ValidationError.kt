package lab.maxb.dark_api.domain.exceptions

class ValidationError(
    val errors: List<String>
) : DomainException("Validation error") {
    constructor(vararg errors: String) : this(errors.toList())

    class Builder {
        private val errors = mutableListOf<String>()

        fun addError(error: String) = errors.add(error)
        fun build() = ValidationError(errors.toList())
    }
}

inline fun <T> validate(crossinline block: ValidationError.Builder.() -> T): T {
    val result: T
    val error = ValidationError.Builder().apply {
        result = block()
    }.build()
    if (error.errors.isNotEmpty())
        throw error
    return result
}