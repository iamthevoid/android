package com.pg.homeworknetwork.ui.list

import com.pg.homeworknetwork.data.Movies
import com.pg.homeworknetwork.mvp.MvpView

interface MoviesListView: MvpView {

    fun showMovies(list: List<Movies.Short>)

    fun showLoading()

    fun hideLoading()

    fun showRefreshing()

    fun hideRefreshing()
}