package domain

import domain.purchase.policy.screening.MovieDayDiscountPolicy
import kotlinx.datetime.LocalDateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MovieDayDiscountPolicyTest {
    private val policy = MovieDayDiscountPolicy()

    @Test
    fun `매월 10일에 상영되는 영화는 10퍼센트 할인된다`() {
        val result = policy.apply(10_000, LocalDateTime(2026, 4, 10, 14, 0))

        assertEquals(9_000, result)
    }

    @Test
    fun `매월 20일에 상영되는 영화는 10퍼센트 할인된다`() {
        val result = policy.apply(10_000, LocalDateTime(2026, 4, 20, 14, 0))

        assertEquals(9_000, result)
    }

    @Test
    fun `매월 30일에 상영되는 영화는 10퍼센트 할인된다`() {
        val result = policy.apply(10_000, LocalDateTime(2026, 4, 30, 14, 0))

        assertEquals(9_000, result)
    }

    @Test
    fun `무비데이가 아닌 날에는 할인이 적용되지 않는다`() {
        val result = policy.apply(10_000, LocalDateTime(2026, 4, 1, 14, 0))

        assertEquals(10_000, result)
    }
}
