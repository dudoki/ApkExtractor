package com.droid.app.extractor.data.source

import com.droid.app.extractor.data.App

interface AppsDataSource {

    interface LoadAppsCallback {

        fun onAppsLoaded(apps: List<App>)

    }

    interface ExportAppsCallback {

        fun onAppPreProcess(app: App)

        fun onAppPostProcess(app: App)

        fun onAppsExported()

    }

    fun getApps(callback: LoadAppsCallback)

    fun exportApps(apps: Iterable<App>, callback: ExportAppsCallback)
}
