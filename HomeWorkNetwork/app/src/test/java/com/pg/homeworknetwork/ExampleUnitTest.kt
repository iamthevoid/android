package com.pg.homeworknetwork

import com.pg.homeworknetwork.data.Movie
import com.pg.homeworknetwork.ui.movie.MovieDetailPresenter
import com.pg.homeworknetwork.ui.movie.MovieDetailView
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Assert
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val presenter = MovieDetailPresenter(550, object : MovieDetailView {
            override fun onTitleCalculated(titleRes: Int, title: String) {
                println(title)
            }

            override fun onMovieDataReceived(movie: Movie) {
                println(movie)
                Assert.assertEquals(movie.originalTitle, "Moonfall")
            }
        }, scheduler = Schedulers.trampoline(), ioScheduler = Schedulers.trampoline())
        presenter.onCreate()
    }
}