package service

import domain.user.User

class PointUsageService(
    private val user: User,
) {
    fun apply(
        totalPrice: Int,
        input: String,
    ): Pair<Int, Int> {
        val usedPoint = input.toInt()
        user.point.discount(usedPoint)

        return totalPrice - usedPoint to usedPoint
    }
}
