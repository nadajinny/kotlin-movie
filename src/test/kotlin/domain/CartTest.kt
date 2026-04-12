package domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CartTest {
    @Test
    fun `동일한 시간대에 이미 예매한 내역이 있으면 예외가 발생한다`() {
        val showing = TestFixtureData.showings[0]

        val exception = assertThrows<IllegalArgumentException> {
            TestFixtureData.cart.checkReservationHistory(showing)
        }

        assertEquals("선택하신 상영 시간이 겹칩니다. 다른 시간을 선택해 주세요.", exception.message)
    }
}
