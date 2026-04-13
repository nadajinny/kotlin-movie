package service

import domain.purchase.Calculator
import domain.reservation.Cart

class PriceCalculationService(
    private val cart: Cart,
) {
    fun calculateDiscountedPrice(): Int =
        cart.reservationInfos.sumOf {
            Calculator.calculateByMovie(
                price = it.seat.grade.price,
                date = it.showing.startTime,
            )
        }
}
