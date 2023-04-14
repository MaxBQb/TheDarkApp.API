package lab.maxb.dark_api.application.response

data class ResponseError(
    val message: String,
    val details: List<String> = emptyList(),
)