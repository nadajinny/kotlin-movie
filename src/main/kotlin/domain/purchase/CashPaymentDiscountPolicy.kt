package domain.purchase

class CashPaymentDiscountPolicy : PaymentDiscountPolicy {
    override fun supports(method: PaymentMethod): Boolean {
        return method == PaymentMethod.CASH
    }

    override fun apply(price: Int): Int {
        return ((1 - DISCOUNT_PERCENT) * price).toInt()
    }

    companion object {
        const val DISCOUNT_PERCENT = 0.02
    }
}
