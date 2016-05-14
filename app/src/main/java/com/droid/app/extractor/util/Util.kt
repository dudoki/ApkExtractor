package com.droid.app.extractor.util

import java.util.*

fun convertUnit(value: Long = 0): String {
    val units = arrayOf("KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB", "DB", "NB")
    val unit = 1024.0
    val length = units.size

    var size: Double = value.toDouble()
    var index = 0

    if (size < unit) {
        return String.format(Locale.getDefault(), "%.0f Bytes", size)
    }

    while (index < length) {
        size /= unit
        if (size < unit) {
            return String.format(Locale.getDefault(), "%.2f %s", size, units[index])
        }
        index++
    }
    throw IllegalArgumentException();
}