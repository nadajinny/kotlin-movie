package persistence

import domain.reservation.ReservationInfo
import domain.seat.SeatState
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.UUID

class CinemaDatabaseTest {
    @Test
    fun `기본 상영 데이터를 저장한 뒤 다시 조회할 수 있다`() {
        val database = CinemaDatabase.inMemory(UUID.randomUUID().toString())

        val movieTheater = database.loadMovieTheater()

        assertThat(movieTheater.movies.map { it.id.value }).isEqualTo(listOf("movie-f1", "movie-iron-man", "movie-toy-story"))
        assertThat(movieTheater.movies.map { it.title }).isEqualTo(listOf("F1 더 무비", "아이언맨", "토이 스토리"))
        assertThat(movieTheater.screens.map { it.id.value }.sorted()).isEqualTo(listOf("screen-1", "screen-2", "screen-3"))
        assertThat(movieTheater.screenings.map { "${it.movie.id.value}/${it.screen.id.value}/${it.startTime}" }.sorted())
            .isEqualTo(
                listOf(
                    "movie-f1/screen-1/2025-09-20T10:20",
                    "movie-f1/screen-1/2025-09-20T13:00",
                    "movie-f1/screen-1/2025-09-20T15:40",
                    "movie-f1/screen-1/2025-09-20T20:10",
                    "movie-iron-man/screen-3/2025-09-20T09:50",
                    "movie-toy-story/screen-2/2025-09-20T13:30",
                    "movie-toy-story/screen-2/2025-09-20T16:00",
                ),
            )
    }

    @Test
    fun `예매 정보를 저장한 뒤 다시 조회할 수 있다`() {
        val database = CinemaDatabase.inMemory(UUID.randomUUID().toString())
        val movieTheater = database.loadMovieTheater()
        val firstScreening = movieTheater.screenings.first()
        val secondScreening = movieTheater.screenings.last()
        val reservations =
            listOf(
                ReservationInfo(firstScreening, firstScreening.screen.findSeat('C', 1)!!),
                ReservationInfo(secondScreening, secondScreening.screen.findSeat('E', 4)!!),
            )

        database.saveReservations(reservations)

        val actual = database.findReservations()
        val expected =
            reservations
                .sortedBy { it.screening.startTime.toString() }
                .map {
                    "${it.screening.movie.id.value}/${it.screening.screen.id.value}/${it.screening.startTime}/${it.seat.coordinate.row}${it.seat.coordinate.column}"
                }

        assertThat(actual).hasSize(2)
        assertThat(
            actual.map {
                "${it.screening.movie.id.value}/${it.screening.screen.id.value}/${it.screening.startTime}/${it.seat.coordinate.row}${it.seat.coordinate.column}"
            },
        ).isEqualTo(expected)
        assertThat(actual).allSatisfy { assertThat(it.seat.isReserved).isEqualTo(SeatState.RESERVED) }
    }
}
