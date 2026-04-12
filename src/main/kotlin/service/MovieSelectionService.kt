package service

import domain.cinema.Movie
import domain.cinema.MovieTheater
import util.ErrorMessage

class MovieSelectionService(private val movieTheater: MovieTheater) {
    fun selectByTitle(title: String): Movie {
        return requireNotNull(movieTheater.findMovie(title)) { ErrorMessage.MOVIE_NOT_FOUND }
    }
}
