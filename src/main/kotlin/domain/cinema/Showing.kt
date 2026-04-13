package domain.cinema

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.minutes

class Showing(
    val startTime: LocalDateTime,
    val screen: Screen,
    val movie: Movie,
) {
    val endTime =
        startTime
            .toInstant(TimeZone.currentSystemDefault())
            .plus(movie.runningTime.minutes)
            .toLocalDateTime(TimeZone.currentSystemDefault())
}
