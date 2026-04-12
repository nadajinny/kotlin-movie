package service

import domain.cinema.Movie
import domain.cinema.MovieTheater

class MovieSelectionService(private val movieTheater: MovieTheater) {
    fun selectByTitle(title: String): Movie {
        return requireNotNull(movieTheater.findMovie(title)) { "존재하지 않는 영화입니다." }
    }
}
