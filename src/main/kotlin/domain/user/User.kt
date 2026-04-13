package domain.user

import domain.Id
import util.ErrorMessage

class User(
    val id: Id,
    var point: Point = Point(2000),
) {
    init {
        require(id.value > 0) { ErrorMessage.ID_MUST_BE_POSITIVE }
    }

    fun discountPoint(discount: Int) {
        point = point.discount(discount)
    }
}
