package com.droid.app.extractor.apps

import com.droid.app.extractor.BasePresenter
import com.droid.app.extractor.BaseView
import com.droid.app.extractor.data.App

interface AppsContract {

    interface Presenter : BasePresenter {

        fun loadApps()

        fun exportApps(apps: Iterable<App>)

    }

    interface View : BaseView<Presenter> {

        fun setLoadingIndicator(active: Boolean)

        fun setExportIndicator(active: Boolean)

        fun showApps(apps: List<App>)

        fun showNoApps()

        fun showExportProgress(app: App)

        fun updateItemState(app: App)
    }


}
