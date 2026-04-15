package service

import domain.purchase.Calculator
import domain.reservation.Cart

class PriceCalculationService {
    fun calculateDiscountedPrice(cart: Cart): Int =
        cart.reservationInfos.sumOf {
            Calculator.calculateByMovie(
                price = it.seat.grade.price,
                date = it.screening.startTime,
            )
        }
}
