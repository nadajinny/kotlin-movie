package domain.purchase

import domain.purchase.policy.payment.CardPaymentDiscountPolicy
import domain.purchase.policy.payment.CashPaymentDiscountPolicy
import domain.purchase.policy.payment.PaymentDiscountPolicy
import domain.purchase.policy.screening.MovieDayDiscountPolicy
import domain.purchase.policy.screening.ShowTimeDiscountPolicy
import domain.purchase.policy.screening.ScreeningDiscountPolicy
import domain.user.User
import kotlinx.datetime.LocalDateTime

object Calculator {
    private val movieDayDiscountPolicy = MovieDayDiscountPolicy()
    private val showTimeDiscountPolicy = ShowTimeDiscountPolicy()
    private val screeningDiscountPolicies: List<ScreeningDiscountPolicy> =
        listOf(
            movieDayDiscountPolicy,
            showTimeDiscountPolicy,
        )
    private val paymentDiscountPolicies: List<PaymentDiscountPolicy> =
        listOf(
            CardPaymentDiscountPolicy(),
            CashPaymentDiscountPolicy(),
        )

    fun subtractUserPoint(
        price: Int,
        user: User,
        subtractPoint: Int,
    ): Int {
        user.discountPoint(subtractPoint)
        return price - subtractPoint
    }

    fun calculateByMovie(
        price: Int,
        date: LocalDateTime,
    ): Int =
        screeningDiscountPolicies.fold(price) { discountedPrice, policy ->
            policy.apply(discountedPrice, date)
        }

    fun applyMovieDayDiscount(
        price: Int,
        date: LocalDateTime,
    ): Int = movieDayDiscountPolicy.apply(price, date)

    fun applyTimeDiscount(
        price: Int,
        date: LocalDateTime,
    ): Int = showTimeDiscountPolicy.apply(price, date)

    fun applyPaymentDiscount(
        price: Int,
        method: PaymentMethod,
    ): Int {
        val policy = paymentDiscountPolicies.first { it.supports(method) }
        return policy.apply(price)
    }
}
