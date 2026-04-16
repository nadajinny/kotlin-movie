package persistence

import domain.cinema.MovieTheater
import domain.reservation.ReservationInfo
import persistence.db.DatabaseInitializer
import persistence.db.JdbcDatabase
import persistence.repository.JdbcMovieTheaterRepository
import persistence.repository.JdbcReservationRepository
import persistence.seed.CinemaSeedData
import java.nio.file.Path

class CinemaDatabase private constructor(
    private val database: JdbcDatabase,
) {
    private val movieTheaterRepository = JdbcMovieTheaterRepository(database)
    private val reservationRepository = JdbcReservationRepository(database)

    init {
        DatabaseInitializer(database).initialize()
        movieTheaterRepository.initialize(CinemaSeedData.movieTheater())
    }

    fun loadMovieTheater(): MovieTheater = movieTheaterRepository.find()

    fun saveReservations(reservationInfos: List<ReservationInfo>) {
        reservationRepository.saveAll(reservationInfos)
    }

    fun findReservations(): List<ReservationInfo> = reservationRepository.findAll()

    companion object {
        fun local(databasePath: Path): CinemaDatabase = CinemaDatabase(JdbcDatabase.file(databasePath))

        fun inMemory(name: String): CinemaDatabase = CinemaDatabase(JdbcDatabase.inMemory(name))
    }
}
