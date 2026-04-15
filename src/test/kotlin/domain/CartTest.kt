package domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import util.ErrorMessage

class CartTest {
    @Test
    fun `동일한 시간대에 이미 예매한 내역이 있으면 예외가 발생한다`() {
        val screening = TestFixtureData.screenings[0]

        val exception =
            assertThrows<IllegalArgumentException> {
                TestFixtureData.cart.checkReservationHistory(screening)
            }

        assertEquals(ErrorMessage.OVERLAPPING_SCREENING, exception.message)
    }
}
