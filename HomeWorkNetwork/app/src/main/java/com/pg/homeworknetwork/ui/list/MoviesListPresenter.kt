package com.pg.homeworknetwork.ui.list

import com.pg.homeworknetwork.data.Client
import com.pg.homeworknetwork.data.Movies
import com.pg.homeworknetwork.mvp.MvpPresenter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class MoviesListPresenter(view: MoviesListView) : MvpPresenter<MoviesListView>(view, AndroidSchedulers.mainThread()) {

    private val movies = mutableListOf<Movies.Short>()
    private var page: Int = 1

    fun refresh() {
        page = 1
        load(
            onStartLoad = { mvpView.showRefreshing() },
            onLoadFinished = { mvpView.hideRefreshing() }
        )
    }

    fun onCreate() {
        page = 1
        load(onStartLoad = { mvpView.showLoading() }, onLoadFinished = { mvpView.hideLoading() })
    }

    fun loadMore() {
        page++
        load(onStartLoad = { mvpView.showLoading() }, onLoadFinished = { mvpView.hideLoading() })
    }

    private fun load(onStartLoad: () -> Unit = {}, onLoadFinished: () -> Unit = {}) {
        Client.popular(page)
            .subscribeOn(Schedulers.io())
            .map(Movies::moviesShort)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onStartLoad() }
            .doOnTerminate { onLoadFinished() }
            .subscribe({ onListReceived(page, it) }, Throwable::printStackTrace)
    }

    private fun onListReceived(page: Int, list: List<Movies.Short>) {
        if (page == 1) {
            movies.clear()
        }
        movies.addAll(list)
        mvpView.showMovies(movies)
    }
}