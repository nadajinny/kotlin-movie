package service

import domain.purchase.Calculator
import domain.purchase.PaymentMethod

class PaymentDiscountService {
    fun apply(
        price: Int,
        method: PaymentMethod,
    ): Int = Calculator.applyPaymentDiscount(price, method)
}
