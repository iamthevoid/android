package com.pg.homeworknetwork.ui.movie

import androidx.annotation.StringRes
import com.pg.homeworknetwork.data.Movie
import com.pg.homeworknetwork.mvp.MvpView

interface MovieDetailView : MvpView {
    // Cinema: Moonfall
    fun onTitleCalculated(@StringRes titleRes: Int, title: String)

    fun onMovieDataReceived(movie: Movie)

}