package controller

import util.ErrorMessage

class FlowController {
    fun start(input: String): Boolean {
        require(input == "Y" || input == "N") { ErrorMessage.INVALID_YES_OR_NO_INPUT }

        when (input) {
            "Y" -> return true
        }
        return false
    }
}
