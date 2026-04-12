package domain.seat

import util.ErrorMessage

class SeatCoordinate(val row: Char, val column: Int) {
    init {
        require(row.isUpperCase()) { ErrorMessage.ROW_MUST_BE_UPPERCASE }
        require(column > 0) { ErrorMessage.COLUMN_MUST_BE_POSITIVE }
    }
}
