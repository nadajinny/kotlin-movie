package api

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import persistence.CinemaDatabase
import java.nio.file.Files
import java.nio.file.Paths

@Configuration
class CinemaDatabaseConfiguration {
    @Bean
    fun cinemaDatabase(
        @Value("\${app.database.mode:file}") databaseMode: String,
        @Value("\${app.database.name:movie-ticketing}") databaseName: String,
    ): CinemaDatabase =
        when (databaseMode.lowercase()) {
            "memory" -> CinemaDatabase.inMemory(databaseName)

            else -> {
                val databaseDirectory = Paths.get("storage")
                Files.createDirectories(databaseDirectory)
                CinemaDatabase.local(databaseDirectory.resolve(databaseName))
            }
        }
}
