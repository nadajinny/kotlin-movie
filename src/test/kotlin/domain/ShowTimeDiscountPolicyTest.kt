package domain

import domain.purchase.ShowTimeDiscountPolicy
import kotlinx.datetime.LocalDateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ShowTimeDiscountPolicyTest {
    private val policy = ShowTimeDiscountPolicy()

    @Test
    fun `오전 11시 이전에 시작하는 상영은 2000원이 할인된다`() {
        val result = policy.apply(10_000, LocalDateTime(2026, 4, 8, 10, 0))

        assertEquals(8_000, result)
    }

    @Test
    fun `오후 8시 이후에 시작하는 상영은 2000원이 할인된다`() {
        val result = policy.apply(10_000, LocalDateTime(2026, 4, 8, 21, 0))

        assertEquals(8_000, result)
    }

    @Test
    fun `오전 11시부터 오후 8시 사이에 시작하는 상영은 시간 할인이 적용되지 않는다`() {
        val result = policy.apply(10_000, LocalDateTime(2026, 4, 8, 16, 0))

        assertEquals(10_000, result)
    }
}
