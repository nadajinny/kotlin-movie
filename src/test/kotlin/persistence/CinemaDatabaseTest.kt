package persistence

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.UUID

class CinemaDatabaseTest {
    @Test
    fun `기본 상영 데이터를 저장한 뒤 다시 조회할 수 있다`() {
        val database = CinemaDatabase.inMemory(UUID.randomUUID().toString())

        val movieTheater = database.loadMovieTheater()

        assertEquals(listOf("movie-f1", "movie-iron-man", "movie-toy-story"), movieTheater.movies.map { it.id.value })
        assertEquals(listOf("F1 더 무비", "아이언맨", "토이 스토리"), movieTheater.movies.map { it.title })
        assertEquals(listOf("screen-1", "screen-2", "screen-3"), movieTheater.screens.map { it.id.value }.sorted())
        assertEquals(
            listOf(
                "movie-f1/screen-1/2025-09-20T10:20",
                "movie-f1/screen-1/2025-09-20T13:00",
                "movie-f1/screen-1/2025-09-20T15:40",
                "movie-f1/screen-1/2025-09-20T20:10",
                "movie-iron-man/screen-3/2025-09-20T09:50",
                "movie-toy-story/screen-2/2025-09-20T13:30",
                "movie-toy-story/screen-2/2025-09-20T16:00",
            ),
            movieTheater.screenings.map { "${it.movie.id.value}/${it.screen.id.value}/${it.startTime}" }.sorted(),
        )
    }
}
