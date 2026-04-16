package persistence.db

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.UUID

class JdbcDatabaseTest {
    @Test
    fun `H2 데이터베이스 연결이 정상적으로 생성된다`() {
        val database = JdbcDatabase.inMemory(UUID.randomUUID().toString())

        val actual =
            database.withConnection { connection ->
                connection.createStatement().use { statement ->
                    statement.executeQuery("select 1").use { resultSet ->
                        resultSet.next()
                        resultSet.getInt(1)
                    }
                }
            }

        assertEquals(1, actual)
    }
}
