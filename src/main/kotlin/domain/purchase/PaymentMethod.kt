package domain.purchase

import util.ErrorMessage

enum class PaymentMethod {
    CARD,
    CASH,
    ;

    companion object {
        fun from(input: String): PaymentMethod {
            val methodNumber = input.toIntOrNull()
            require(methodNumber != null && methodNumber in 1..entries.size) { ErrorMessage.INVALID_PAYMENT_METHOD }

            return entries[methodNumber - 1]
        }
    }
}
