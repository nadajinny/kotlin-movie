package controller

import domain.reservation.Cart
import domain.user.User
import domain.purchase.PaymentMethod
import service.PaymentDiscountService
import service.PointUsageService
import service.PriceCalculationService
import util.retryOnInvalidInput
import view.InputView
import view.OutputView

class PaymentController(
) {
    private val priceCalculationService = PriceCalculationService()
    private val pointUsageService = PointUsageService()
    private val paymentDiscountService = PaymentDiscountService()

    fun run(
        cart: Cart,
        user: User,
    ): Pair<Int, Int> {
        var price = discountPerSeat(cart)
        val pair = retryOnInvalidInput(OutputView::printError) { getUserPoint(user, price) }
        price = pair.first

        price = retryOnInvalidInput(OutputView::printError) { getPaymentMethod(price) }

        OutputView.printTotalPrice(price)

        return price to pair.second
    }

    fun getUserPoint(
        user: User,
        totalPrice: Int,
    ): Pair<Int, Int> {
        val input = InputView.readPoint()
        return pointUsageService.apply(user, totalPrice, input)
    }

    fun discountPerSeat(cart: Cart): Int = priceCalculationService.calculateDiscountedPrice(cart)

    fun getPaymentMethod(price: Int): Int {
        val method: PaymentMethod = InputView.readPaymentMethod()
        return paymentDiscountService.apply(price, method)
    }

    fun confirmPayment(
        user: User,
        usedPoint: Int,
    ) {
        user.discountPoint(usedPoint)
    }
}
