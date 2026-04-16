import controller.CartController
import controller.FlowController
import controller.PaymentController
import controller.ReservationController
import domain.Id
import domain.reservation.Cart
import domain.user.User
import persistence.CinemaDatabase
import util.retryOnInvalidInput
import view.InputView
import view.OutputView
import java.nio.file.Paths

fun main() {
    val databaseDirectory = Paths.get("storage")
    databaseDirectory.toFile().mkdirs()
    val cinemaDatabase = CinemaDatabase.local(databaseDirectory.resolve("movie-ticketing"))
    val movieTheater = cinemaDatabase.loadMovieTheater()

    var cart =
        Cart(
            reservationInfos = listOf(),
        )
    val user =
        User(
            Id("user-main"),
        )
    val cartController = CartController()
    val flowController = FlowController()
    val reservationController =
        ReservationController(
            movieTheater = movieTheater,
        )
    val paymentController = PaymentController()

    val shouldStart =
        retryOnInvalidInput(OutputView::printError) {
            flowController.start(InputView.startTicketing())
        }
    if (!shouldStart) return

    var shouldContinue = true
    while (shouldContinue) {
        val pair = reservationController.run(cart)

        cart =
            cartController.run(
                cart = cart,
                screening = pair.first,
                seats = pair.second,
            )

        shouldContinue =
            retryOnInvalidInput(OutputView::printError) {
                flowController.start(InputView.continueTicketing())
            }
    }

    val receipt = paymentController.run(cart, user)
    val confirm =
        retryOnInvalidInput(OutputView::printError) {
            flowController.start(InputView.readPurchaseConfirm())
        }
    if (!confirm) return
    paymentController.confirmPayment(user, receipt)
    cinemaDatabase.saveReservations(receipt.purchaseHistory)

    OutputView.printTotal(receipt)
}
