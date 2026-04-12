package domain.purchase

interface PaymentDiscountPolicy {
    fun supports(method: PaymentMethod): Boolean

    fun apply(price: Int): Int
}
