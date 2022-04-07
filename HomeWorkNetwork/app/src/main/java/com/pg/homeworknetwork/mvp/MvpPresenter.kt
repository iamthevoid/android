package com.pg.homeworknetwork.mvp

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler

abstract class MvpPresenter<VIEW: MvpView>(protected val mvpView: VIEW, val androidScheduler: Scheduler) {
}