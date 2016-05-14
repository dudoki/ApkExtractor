package com.droid.app.extractor.data.source

import android.content.Context
import android.content.pm.ApplicationInfo
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import com.droid.app.extractor.data.App
import com.droid.app.extractor.util.FileUtils
import com.droid.app.extractor.util.e
import java.util.*

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

    override fun exportApps(apps: List<App>, callback: AppsDataSource.ExportAppsCallback) {
        object : AsyncTask<Void, App, Void>() {

            override fun doInBackground(vararg params: Void): Void? {
                if (apps.isEmpty()) {
                    return null
                }

                for (app in apps) {
                    with(app) {
                        processed = false
                        publishProgress(app)
                    }

                    exportApp(app)

                    with(app) {
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

        internal fun resolvePackage(context: Context): List<App> {
            val apps = ArrayList<App>()

            val packageManager = context.packageManager
            val packages = packageManager.getInstalledPackages(0)
            for (pack in packages) {
                val appInfo = pack.applicationInfo

                if (isSystemApp(appInfo)) {
                    continue
                }

                val packageName = appInfo.packageName
                var icon: Drawable? = appInfo.loadIcon(packageManager)
                val label = appInfo.loadLabel(packageManager).toString()
                val path = appInfo.sourceDir
                val versionName = pack.versionName

                if (icon == null) {
                    icon = packageManager.defaultActivityIcon
                }

                apps.add(App(packageName, icon!!, label, path, versionName))
            }

            return apps
        }

        internal fun isSystemApp(appInfo: ApplicationInfo): Boolean {
            return appInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0 && appInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP == 0
        }

        internal fun exportApp(app: App) {
            try {
                FileUtils.copyFile(app.srcFile, app.destFile)
            } catch (ex: Exception) {
                e(ex.message, ex)
            }

        }
    }
}
