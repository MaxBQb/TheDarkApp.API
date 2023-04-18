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

inline fun <T> T.applyValidation(crossinline block: ValidationError.ValidationContext.() -> Unit): T = apply {
    ValidationError
        .ValidationContext().apply(block)
        .doThrow()
}