package com.pg.homeworknetwork.ui.movie

import com.pg.homeworknetwork.R
import com.pg.homeworknetwork.data.Client
import com.pg.homeworknetwork.mvp.MvpPresenter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.internal.schedulers.IoScheduler
import io.reactivex.rxjava3.schedulers.Schedulers

class MovieDetailPresenter(
    val id: Int,
    view: MovieDetailView,
    scheduler: Scheduler = AndroidSchedulers.mainThread(),
    val ioScheduler: Scheduler = Schedulers.io()
) : MvpPresenter<MovieDetailView>(view, scheduler) {

    fun onCreate() {
        Client.movie(id)
            .subscribeOn(ioScheduler)
            .observeOn(androidScheduler)
            .subscribe(mvpView::onMovieDataReceived, Throwable::printStackTrace)
        mvpView.onTitleCalculated(R.string.page_title, "Moonfall")
    }
}