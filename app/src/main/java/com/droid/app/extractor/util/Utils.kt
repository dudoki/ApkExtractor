package com.droid.app.extractor.util

import android.content.Context
import android.content.pm.ApplicationInfo
import android.graphics.drawable.Drawable
import com.droid.app.extractor.data.App
import java.util.*

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