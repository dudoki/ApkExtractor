package com.droid.app.extractor

import android.app.Application
import android.os.Environment
import com.droid.app.extractor.util.FileUtils
import com.droid.app.extractor.util.e
import java.io.File

class AppsApplication : Application() {

    companion object {
        val destDir: File
            get() {
                val directory = File(Environment.getExternalStorageDirectory(), "apks")
                try {
                    FileUtils.forceMkdir(directory)
                } catch(ex: Exception) {
                    e(ex.message, ex)
                }
                return directory
            }
    }

}
