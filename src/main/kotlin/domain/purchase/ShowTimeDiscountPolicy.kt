package domain.purchase

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

class ShowTimeDiscountPolicy : ShowingDiscountPolicy {
    override fun apply(
        price: Int,
        date: LocalDateTime,
    ): Int {
        return if (date.time > NONE_DISCOUNT_TIME_BOUNDARY.first && date.time < NONE_DISCOUNT_TIME_BOUNDARY.second) {
            price
        } else {
            price - DISCOUNT_PRICE
        }
    }

    companion object {
        private val NONE_DISCOUNT_TIME_BOUNDARY = LocalTime(11, 0) to LocalTime(20, 0)
        const val DISCOUNT_PRICE = 2000
    }
}
