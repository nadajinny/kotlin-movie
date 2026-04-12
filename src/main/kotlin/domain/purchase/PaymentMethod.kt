package domain.purchase

enum class PaymentMethod {
    CARD,
    CASH,
    ;

    companion object {
        fun from(input: String): PaymentMethod {
            val methodNumber = input.toIntOrNull()
            require(methodNumber != null && methodNumber in 1..entries.size) { "유효하지 않은 결제 수단입니다." }

            return entries[methodNumber - 1]
        }
    }
}
