package domain

import domain.reservation.Reservation
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ReservationTest {
    @Test
    fun `동일한 시간대에 이미 예매한 내역이 있으면 예외가 발생한다`() {
        // given : 사용자의 id 는 1이다. 사용자는 해당 시간에 예매한 내역이 있다.
        val showing = TestFixtureData.showings[0]

        // when : 사용자가 해당 시간에 예약하려고 할 때
        val exception = assertThrows<IllegalArgumentException> {
            Reservation.checkReservationHistory(TestFixtureData.reservationInfos, showing)
        }
        // then : 예외가 발생한다.
        assertEquals("선택하신 상영 시간이 겹칩니다. 다른 시간을 선택해 주세요.", exception.message)
    }
}
