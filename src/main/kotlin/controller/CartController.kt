package controller

import domain.cinema.Showing
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
        showing: Showing,
        seats: List<Seat>,
    ): Cart {
        addAllReservationInfo(showing, seats)
        showCart()
        return cart
    }

    fun addAllReservationInfo(
        showing: Showing,
        seats: List<Seat>,
    ) {
        cart = cart.addAll(showing, seats)
    }

    fun addReservationInfo(reservationInfo: ReservationInfo) {
        cart = cart.addInfo(reservationInfo)
    }

    fun getAllReservationInfo(): List<String> = cart.getAllReservationInfo()

    fun showCart() {
        OutputView.printCart(getAllReservationInfo())
    }
}
