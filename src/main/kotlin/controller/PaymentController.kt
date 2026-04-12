package controller

import domain.reservation.Cart
import domain.user.User
import service.PaymentDiscountService
import service.PointUsageService
import service.PriceCalculationService
import view.InputView
import view.OutputView

class PaymentController(val cart: Cart, val user: User) {
    private val priceCalculationService = PriceCalculationService(cart)
    private val pointUsageService = PointUsageService(user)
    private val paymentDiscountService = PaymentDiscountService()

    fun run(): Pair<Int, Int> {
        var price = discountPerSeat()
        val pair = getUserPoint(price)
        price = pair.first

        price = getPaymentMethod(price)

        OutputView.printTotalPrice(price)

        return price to pair.second
    }

    fun getUserPoint(totalPrice: Int): Pair<Int, Int> {
        val input = InputView.readPoint()
        return pointUsageService.apply(totalPrice, input)
    }

    fun discountPerSeat(): Int {
        return priceCalculationService.calculateDiscountedPrice()
    }

    fun getPaymentMethod(price: Int): Int {
        val input = InputView.readPaymentMethod()
        return paymentDiscountService.apply(price, input)
    }
}
