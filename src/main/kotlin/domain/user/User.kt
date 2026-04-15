package domain.user

import domain.Id

class User(
    val id: Id,
    var point: Point = Point(2000),
) {
    fun discountPoint(discount: Int) {
        point = point.discount(discount)
    }
}
