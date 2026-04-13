package util

fun <T> retryOnInvalidInput(
    onError: (String) -> Unit,
    action: () -> T,
): T {
    while (true) {
        try {
            return action()
        } catch (exception: IllegalArgumentException) {
            onError(exception.message ?: ErrorMessage.INVALID_INPUT)
        }
    }
}
