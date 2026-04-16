package api

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import persistence.CinemaDatabase
import java.util.UUID

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MovieApiControllerTest(
    @param:Autowired private val webApplicationContext: WebApplicationContext,
) {
    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
    }

    @Test
    fun `영화 목록 조회 요청 시 200 OK를 반환한다`() {
        val result =
            mockMvc.perform(
                get("/api/movies")
                    .accept(MediaType.APPLICATION_JSON),
            )

        result
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.movies[0].id").value(1))
            .andExpect(jsonPath("$.movies[0].title").value("F1 더 무비"))
            .andExpect(jsonPath("$.movies[0].runningTimeMinutes").value(130))
            .andExpect(jsonPath("$.movies[0].screenings[0].id").value(101))
            .andExpect(jsonPath("$.movies[0].screenings[0].startAt").value("2025-09-20T10:20:00"))
            .andExpect(jsonPath("$.movies[0].screenings[0].endAt").value("2025-09-20T12:30:00"))
    }

    @Test
    fun `예매 생성 요청 시 201 Created를 반환한다`() {
        val result =
            mockMvc.perform(
                post("/api/reservations")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        """
                        {
                          "reservations": [
                            {
                              "screeningId": 101,
                              "seats": ["C2", "C3"]
                            }
                          ],
                          "usedPoints": 2000,
                          "paymentMethod": "CREDIT_CARD"
                        }
                        """.trimIndent(),
                    ),
            )

        result
            .andExpect(status().isCreated)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.reservationId").exists())
            .andExpect(jsonPath("$.reservations[0].screeningId").value(101))
            .andExpect(jsonPath("$.reservations[0].seats[0]").value("C2"))
            .andExpect(jsonPath("$.usedPoints").value(2000))
            .andExpect(jsonPath("$.paymentMethod").value("CREDIT_CARD"))
            .andExpect(jsonPath("$.totalPrice").value(25080))
    }

    @Test
    fun `이미 예약된 좌석을 예매하려는 경우 409 Conflict를 반환한다`() {
        val request =
            """
            {
              "reservations": [
                {
                  "screeningId": 101,
                  "seats": ["C2"]
                }
              ],
              "usedPoints": 0,
              "paymentMethod": "CREDIT_CARD"
            }
            """.trimIndent()

        val firstResponse =
            mockMvc.perform(
                post("/api/reservations")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request),
            )

        firstResponse
            .andExpect(status().isCreated)

        val secondResponse =
            mockMvc.perform(
                post("/api/reservations")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request),
            )

        secondResponse
            .andExpect(status().isConflict)
            .andExpect(jsonPath("$.message").value("해당 좌석은 이미 예약되었습니다."))
    }

    @Test
    fun `존재하지 않는 상영에 대해 예매를 요청하면 404 Not Found를 반환한다`() {
        val result =
            mockMvc.perform(
                post("/api/reservations")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        """
                        {
                          "reservations": [
                            {
                              "screeningId": 999,
                              "seats": ["A1"]
                            }
                          ],
                          "usedPoints": 0,
                          "paymentMethod": "CREDIT_CARD"
                        }
                        """.trimIndent(),
                    ),
            )

        result
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.message").value("존재하지 않는 상영입니다."))
    }

    @Test
    fun `잘못된 요청 형식에 대해 400 Bad Request를 반환한다`() {
        val result =
            mockMvc.perform(
                post("/api/reservations")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        """
                        {
                          "reservations": [],
                          "usedPoints": 0,
                          "paymentMethod": "CREDIT_CARD"
                        }
                        """.trimIndent(),
                    ),
            )

        result
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.message").value("입력된 값이 유효하지 않습니다."))
    }

    @TestConfiguration
    class TestDatabaseConfiguration {
        @Bean
        @Primary
        fun testCinemaDatabase(): CinemaDatabase = CinemaDatabase.inMemory(UUID.randomUUID().toString())
    }
}
