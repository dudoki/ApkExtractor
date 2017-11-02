package com.droid.app.extractor.data

import android.graphics.drawable.Drawable
import com.droid.app.extractor.AppsApplication
import com.droid.app.extractor.util.FileUtils
import com.droid.app.extractor.util.e
import java.io.File

data class App(val packageName: String,
               val icon: Drawable,
               val label: String,
               private val path: String,
               val versionName: String?) {

    var checked: Boolean = false

    var processed: Boolean = false

    val size: String
        get() {
            val file = File(path)
            return FileUtils.convertUnit(file.length())
        }

    private val srcFile: File
        get() = File(path)

    private val destFile: File
        get() = File(AppsApplication.destDir, "$label-v$versionName.apk")

    val exported: Boolean
        get() {
            return destFile.exists()
        }

    internal fun export() {
        try {
            FileUtils.copyFile(srcFile, destFile)
        } catch (ex: Exception) {
            e(ex.message, ex.cause)
        }
    }
}
