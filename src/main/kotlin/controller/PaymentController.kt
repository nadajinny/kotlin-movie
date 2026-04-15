package controller

import domain.purchase.PaymentMethod
import domain.reservation.Cart
import domain.user.User
import util.retryOnInvalidInput
import view.InputView
import view.OutputView

class PaymentController {
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
        return user.previewPointUsage(totalPrice, input)
    }

    fun discountPerSeat(cart: Cart): Int = cart.totalPrice()

    fun getPaymentMethod(price: Int): Int {
        val method: PaymentMethod = InputView.readPaymentMethod()
        return method.applyDiscount(price)
    }

    fun confirmPayment(
        user: User,
        usedPoint: Int,
    ) {
        user.discountPoint(usedPoint)
    }
}
