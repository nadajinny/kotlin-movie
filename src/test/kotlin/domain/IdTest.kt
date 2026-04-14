package domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import util.ErrorMessage

class IdTest {
    @Test
    fun `ID는 양수일 때 정상 생성 되어야 한다`() {
        val result = assertDoesNotThrow { Id(1) }

        assertEquals(1, result.value)
    }

    @Test
    fun `ID가 음수일 때 예외가 발생한다`() {
        val exception =
            assertThrows<IllegalArgumentException> {
                Id(-1)
            }

        assertEquals(ErrorMessage.ID_MUST_BE_POSITIVE, exception.message)
    }
}
