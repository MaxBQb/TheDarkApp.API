package lab.maxb.dark_api.domain.exceptions

class ValidationError(
    val errors: List<String>
) : DomainException("Validation error") {
    constructor(vararg errors: String) : this(errors.toList())

    class ValidationContext {
        private val errors = mutableListOf<String>()

        fun addError(error: String) = errors.add(error)

        fun addFatalError(error: String) {
            addError(error)
            doThrow()
        }

        fun doThrow() {
            if (errors.isNotEmpty())
                throw build()
        }

        fun build() = ValidationError(errors.toList())
    }
}

inline fun <R> withValidation(crossinline block: ValidationError.ValidationContext.() -> R): R {
    val result: R
    ValidationError
        .ValidationContext().apply {
            result = block()
        }.doThrow()
    return result
}

inline fun <T> T.applyValidation(crossinline block: ValidationError.ValidationContext.() -> Unit) = withValidation {
    block()
    this@T
}