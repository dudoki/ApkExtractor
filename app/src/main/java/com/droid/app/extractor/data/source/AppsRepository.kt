package com.droid.app.extractor.data.source

import android.content.Context
import android.os.AsyncTask
import com.droid.app.extractor.data.App
import com.droid.app.extractor.util.resolvePackage

class AppsRepository internal constructor(internal val mContext: Context) : AppsDataSource {

    override fun getApps(callback: AppsDataSource.LoadAppsCallback) {

        object : AsyncTask<Void, Void, List<App>>() {
            override fun doInBackground(vararg params: Void): List<App> {
                return resolvePackage(mContext)
            }

            override fun onPostExecute(apps: List<App>) {
                callback.onAppsLoaded(apps)
            }
        }.execute()
    }

    override fun exportApps(apps: Iterable<App>, callback: AppsDataSource.ExportAppsCallback) {
        object : AsyncTask<Void, App, Void>() {

            override fun doInBackground(vararg params: Void): Void? {
                if (apps is java.util.List && apps.isEmpty()) {
                    return null
                }

                apps.forEach { app ->
                    with(app) {
                        processed = false
                        publishProgress(app)
                        export()
                        processed = true
                        publishProgress(app)
                    }
                }
                return null
            }

            override fun onProgressUpdate(vararg apps: App) {
                val app = apps[0]
                with(app) {
                    when (processed) {
                        false -> callback.onAppPreProcess(this)
                        true -> callback.onAppPostProcess(this)
                    }
                }
            }

            override fun onPostExecute(aVoid: Void?) {
                callback.onAppsExported()
            }
        }.execute()
    }

    companion object {

        private var INSTANCE: AppsRepository? = null

        fun getInstance(context: Context): AppsRepository {
            if (INSTANCE == null) {
                INSTANCE = AppsRepository(context)
            }

            return INSTANCE!!
        }
    }
}
