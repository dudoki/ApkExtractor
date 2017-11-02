package com.droid.app.extractor

interface BaseView<in T> {

    fun setPresenter(presenter: T)

}
