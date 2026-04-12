package domain.user

import util.ErrorMessage

class Point(val value: Int) {
    fun discount(discount: Int): Point {
        require(value - discount >= 0) { ErrorMessage.POINT_DEDUCTION_EXCEEDS_BALANCE }

        return Point(value - discount)
    }
}
