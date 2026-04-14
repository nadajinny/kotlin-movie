package controller

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CartControllerTest {
    val controller: CartController = CartController()

    @Test
    fun `장바구니에 예매 항목을 추가할 수 있다`() {
        // given : 선택한 상영과 좌석 정보가 주어진다.
        val screening = TestFixtureData.screenings.first()
        val seats = listOf(TestFixtureData.seats[2], TestFixtureData.seats[3])

        // when : 장바구니에 예매 항목을 추가하면
        controller.addAllReservationInfo(
            screening = screening,
            seats = seats,
        )

        val result = controller.cart

        // then :
        assertThat(result.reservationInfos).hasSize(2)
    }

    @Test
    fun `장바구니에 담긴 전체 항목을 조회할 수 있다`() {
        // given : 선택한 상영과 좌석 정보가 주어지고 장바구니에 예매 항목을 추가한다.
        val screening = TestFixtureData.screenings.first()
        val seats = listOf(TestFixtureData.seats[2], TestFixtureData.seats[3])

        controller.addAllReservationInfo(
            screening = screening,
            seats = seats,
        )

        // when : 장바구니에 담긴 전체 항목을 조회하면
        val result = controller.getAllReservationInfo()

        // then : 전체 항목이 반환된다.
        assertThat(result).containsExactly("- [해리 포터] 2026-04-10 10:00 좌석: B1, B2")
    }
}
