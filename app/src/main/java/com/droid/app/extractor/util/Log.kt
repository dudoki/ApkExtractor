package com.droid.app.extractor.util

import android.util.Log
import com.droid.app.extractor.BuildConfig

fun Any.v(msg: String?): Unit {
    if (BuildConfig.DEBUG)
        Log.v(javaClass.simpleName, msg)
}

fun Any.v(msg: String?, cause: Throwable?): Unit {
    if (BuildConfig.DEBUG)
        Log.v(javaClass.simpleName, msg, cause)
}

fun Any.d(msg: String?): Unit {
    if (BuildConfig.DEBUG)
        Log.d(javaClass.simpleName, msg)
}

fun Any.d(msg: String?, cause: Throwable?): Unit {
    if (BuildConfig.DEBUG)
        Log.d(javaClass.simpleName, msg, cause)
}

fun Any.i(msg: String?): Unit {
    if (BuildConfig.DEBUG)
        Log.i(javaClass.simpleName, msg)
}

fun Any.i(msg: String?, cause: Throwable?): Unit {
    if (BuildConfig.DEBUG)
        Log.i(javaClass.simpleName, msg, cause)
}

fun Any.w(msg: String?): Unit {
    Log.w(javaClass.simpleName, msg)
}

fun Any.w(msg: String?, cause: Throwable?): Unit {
    Log.w(javaClass.simpleName, msg, cause)
}

fun Any.e(msg: String?): Unit {
    Log.e(javaClass.simpleName, msg)
}

fun Any.e(msg: String?, cause: Throwable?): Unit {
    Log.e(javaClass.simpleName, msg, cause)
}