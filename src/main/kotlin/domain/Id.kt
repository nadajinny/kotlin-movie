package domain

import util.ErrorMessage

data class Id(
    val value: Int,
) {
    init {
        require(value > 0) { ErrorMessage.ID_MUST_BE_POSITIVE }
    }
}
