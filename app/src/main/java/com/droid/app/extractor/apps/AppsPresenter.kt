package com.droid.app.extractor.apps

import com.droid.app.extractor.data.App
import com.droid.app.extractor.data.source.AppsDataSource

class AppsPresenter(internal var mAppsDataSource: AppsDataSource, internal var mAppsView: AppsContract.View) : AppsContract.Presenter {

    init {
        this.mAppsView.setPresenter(this)
    }

    override fun start() {
        loadApps()
    }

    override fun loadApps() {
        loadApps(true)
    }

    internal fun loadApps(showLoadingUI: Boolean) {
        if (showLoadingUI) {
            mAppsView.setLoadingIndicator(true)
        }

        mAppsDataSource.getApps(object : AppsDataSource.LoadAppsCallback {
            override fun onAppsLoaded(apps: List<App>) {
                if (showLoadingUI) {
                    mAppsView.setLoadingIndicator(false)
                }

                processApps(apps)
            }
        })
    }

    internal fun processApps(apps: List<App>) {
        if (apps.isEmpty()) {
            mAppsView.showNoApps()
        } else {
            mAppsView.showApps(apps)
        }
    }

    override fun exportApps(apps: List<App>) {
        exportApps(apps, true)
    }

    internal fun exportApps(apps: List<App>, showLoadingUI: Boolean) {
        if (showLoadingUI) {
            mAppsView.setExportIndicator(true)
        }

        mAppsDataSource.exportApps(apps, object : AppsDataSource.ExportAppsCallback {
            override fun onAppPreProcess(app: App) {
                mAppsView.showExportProgress(app)
            }

            override fun onAppPostProcess(app: App) {
                mAppsView.updateItemState(app)
            }

            override fun onAppsExported() {
                mAppsView.setExportIndicator(false)
            }
        })
    }

}
