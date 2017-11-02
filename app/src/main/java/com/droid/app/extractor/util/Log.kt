package com.droid.app.extractor.util

import android.util.Log
import com.droid.app.extractor.BuildConfig

fun Any.v(msg: String?) {
    if (BuildConfig.DEBUG)
        Log.v(javaClass.simpleName, msg)
}

fun Any.v(msg: String?, cause: Throwable?) {
    if (BuildConfig.DEBUG)
        Log.v(javaClass.simpleName, msg, cause)
}

fun Any.d(msg: String?) {
    if (BuildConfig.DEBUG)
        Log.d(javaClass.simpleName, msg)
}

fun Any.d(msg: String?, cause: Throwable?) {
    if (BuildConfig.DEBUG)
        Log.d(javaClass.simpleName, msg, cause)
}

fun Any.i(msg: String?) {
    if (BuildConfig.DEBUG)
        Log.i(javaClass.simpleName, msg)
}

fun Any.i(msg: String?, cause: Throwable?) {
    if (BuildConfig.DEBUG)
        Log.i(javaClass.simpleName, msg, cause)
}

fun Any.w(msg: String?) {
    Log.w(javaClass.simpleName, msg)
}

fun Any.w(msg: String?, cause: Throwable?) {
    Log.w(javaClass.simpleName, msg, cause)
}

fun Any.e(msg: String?) {
    Log.e(javaClass.simpleName, msg)
}

fun Any.e(msg: String?, cause: Throwable?) {
    Log.e(javaClass.simpleName, msg, cause)
}