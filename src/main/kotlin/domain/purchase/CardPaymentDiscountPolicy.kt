package domain.purchase

class CardPaymentDiscountPolicy : PaymentDiscountPolicy {
    override fun supports(method: PaymentMethod): Boolean {
        return method == PaymentMethod.CARD
    }

    override fun apply(price: Int): Int {
        return ((1 - DISCOUNT_PERCENT) * price).toInt()
    }

    companion object {
        const val DISCOUNT_PERCENT = 0.05
    }
}
