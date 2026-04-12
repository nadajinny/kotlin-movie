package domain

import domain.seat.Seat
import domain.seat.SeatCoordinate
import domain.seat.SeatGrade
import domain.seat.SeatState
import util.ErrorMessage
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertThrows

class SeatTest {
    @Test
    fun `열이 한 글자 알파벳이면 좌석이 정상 생성된다`() {
        // given & when : Seat 객체를 생성할 때 한 글자 알파벳을 입력한다.
        val result = Seat(SeatCoordinate('A', 1), SeatGrade.S, SeatState.AVAILABLE)
        // then : 정상 생성된다.
        assertNotNull(result)
    }

    @Test
    fun `열이 한 글자 대문자 알파벳이 아니면 예외가 발생한다`() {
        // given & when : Seat 객체를 생성할 때 한 글자 소문자 알파벳을 입력한다.
        val exception = assertThrows<IllegalArgumentException> {
            Seat(SeatCoordinate('a', 1), SeatGrade.S, SeatState.AVAILABLE)
        }

        // then : 예외가 발생한다.
        assertEquals(ErrorMessage.ROW_MUST_BE_UPPERCASE, exception.message)
    }

    @Test
    fun `행이 양수이면 좌석이 정상 생성된다`() {
        // given & when : Seat 객체를 생성할 때 행에 양수를 입력한다.
        val result = Seat(SeatCoordinate('A', 1), SeatGrade.S, SeatState.AVAILABLE)
        // then : 정상 생성된다.
        assertNotNull(result)
    }

    @Test
    fun `행이 0이면 예외가 발생한다`() {
        // given & when : Seat 객체를 생성할 때 행에 0을 입력한다.
        val exception = assertThrows<IllegalArgumentException> {
            Seat(SeatCoordinate('A', 0), SeatGrade.S, SeatState.AVAILABLE)
        }

        // then : 예외가 발생한다.
        assertEquals(ErrorMessage.COLUMN_MUST_BE_POSITIVE, exception.message)
    }

    @Test
    fun `행이 음수이면 예외가 발생한다`() {
        // given & when : Seat 객체를 생성할 때 행에 음수를 입력한다.
        val exception = assertThrows<IllegalArgumentException> {
            Seat(SeatCoordinate('A', -1), SeatGrade.S, SeatState.AVAILABLE)
        }

        // then : 예외가 발생한다.
        assertEquals(ErrorMessage.COLUMN_MUST_BE_POSITIVE, exception.message)
    }
}
