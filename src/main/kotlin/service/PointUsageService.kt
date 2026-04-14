package service

import domain.user.User
import util.ErrorMessage

class PointUsageService {
    fun apply(
        user: User,
        totalPrice: Int,
        input: String,
    ): Pair<Int, Int> {
        val usedPoint = input.toIntOrNull()
        require(usedPoint != null && usedPoint >= 0) { ErrorMessage.INVALID_POINT_INPUT }
        user.point.discount(usedPoint)

        return totalPrice - usedPoint to usedPoint
    }
}
