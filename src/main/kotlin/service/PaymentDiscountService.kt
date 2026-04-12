package service

import domain.purchase.Calculator
import domain.purchase.PaymentMethod

class PaymentDiscountService {
    fun apply(
        price: Int,
        input: String,
    ): Int {
        val method = PaymentMethod.from(input)
        return Calculator.applyPaymentDiscount(price, method)
    }
}
