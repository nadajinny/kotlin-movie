package controller

import domain.cinema.Screening
import domain.reservation.Cart
import domain.reservation.ReservationInfo
import domain.seat.Seat
import view.OutputView

class CartController {
    var cart: Cart =
        Cart(
            reservationInfos = listOf(),
        )

    fun run(
        screening: Screening,
        seats: List<Seat>,
    ): Cart {
        addAllReservationInfo(screening, seats)
        showCart()
        return cart
    }

    fun addAllReservationInfo(
        screening: Screening,
        seats: List<Seat>,
    ) {
        cart = cart.addAll(screening, seats)
    }

    fun addReservationInfo(reservationInfo: ReservationInfo) {
        cart = cart.addInfo(reservationInfo)
    }

    fun getAllReservationInfo(): List<String> = cart.getAllReservationInfo()

    fun showCart() {
        OutputView.printCart(getAllReservationInfo())
    }
}
