package domain.reservation

import domain.cinema.Showing

object Reservation {
    fun checkReservationHistory(
        reservationInfos: List<ReservationInfo>,
        showing: Showing,
    ) {
        val history = reservationInfos.filter {
            showing.startTime >= it.showing.startTime && showing.startTime <= it.showing.endTime
        }

        require(history.isEmpty()) { "선택하신 상영 시간이 겹칩니다. 다른 시간을 선택해 주세요." }
    }
}
