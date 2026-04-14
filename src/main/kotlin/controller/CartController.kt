package controller

import domain.cinema.Screening
import domain.reservation.Cart
import domain.reservation.ReservationInfo
import domain.seat.Seat
import view.OutputView

class CartController {
    fun run(
        cart: Cart,
        screening: Screening,
        seats: List<Seat>,
    ): Cart {
        val updatedCart = addAllReservationInfo(cart, screening, seats)
        showCart(updatedCart)
        return updatedCart
    }

    fun addAllReservationInfo(
        cart: Cart,
        screening: Screening,
        seats: List<Seat>,
    ): Cart = cart.addAll(screening, seats)

    fun addReservationInfo(
        cart: Cart,
        reservationInfo: ReservationInfo,
    ): Cart = cart.addInfo(reservationInfo)

    fun getAllReservationInfo(cart: Cart): List<ReservationInfo> = cart.reservationInfos

    fun showCart(cart: Cart) {
        OutputView.printCart(getAllReservationInfo(cart))
    }
}
