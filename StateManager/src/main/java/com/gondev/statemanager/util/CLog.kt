package com.gondev.statemanager.util

import android.text.TextUtils
import android.util.Log
import com.gondev.statemanager.BuildConfig

object CLog {
    @JvmOverloads
    fun v(_msg: String?, depth: Int = 1): Int {
        if (!BuildConfig.DEBUG) return -1
        return if (TextUtils.isEmpty(_msg)) Log.e(makeTag(depth = depth ),
            "CLog message parameter is empty!!") else Log.v(makeTag(depth = depth), _msg!!)
    }

    @JvmOverloads
    fun d(_msg: String?, depth: Int = 1): Int {
        if (!BuildConfig.DEBUG) return -1
        return if (TextUtils.isEmpty(_msg)) Log.e(makeTag(depth = depth ),
            "CLog message parameter is empty!!") else Log.d(makeTag(depth = depth ), _msg!!)
    }

    @JvmOverloads
    fun i(_msg: String?, depth: Int = 1): Int {
        if (!BuildConfig.DEBUG) return -1
        return if (TextUtils.isEmpty(_msg)) Log.e(makeTag(depth = depth ),
            "CLog message parameter is empty!!") else Log.i(makeTag(depth = depth ), _msg!!)
    }

    @JvmOverloads
    fun w(_msg: String?, depth: Int = 1): Int {
        if (!BuildConfig.DEBUG) return -1
        return if (TextUtils.isEmpty(_msg)) Log.e(makeTag(depth = depth ),
            "CLog message parameter is empty!!") else Log.w(makeTag(depth = depth ), _msg!!)
    }

    @JvmOverloads
    fun e(_msg: String?, depth: Int = 1): Int {
        if (!BuildConfig.DEBUG) return -1
        return if (TextUtils.isEmpty(_msg)) Log.e(makeTag(depth = depth ),
            "CLog message parameter is empty!!") else Log.e(makeTag(depth = depth ),
            _msg!!)
    }

    @JvmOverloads
    fun e(_msg: String?, th: Throwable?, depth: Int = 1): Int {
        if (!BuildConfig.DEBUG) return -1
        return if (TextUtils.isEmpty(_msg)) Log.e(makeTag(depth = depth ),
            "CLog message parameter is empty!!") else Log.e(makeTag(depth = depth ), _msg, th)
    }
}

fun makeTag(tag: String="TAG", depth: Int=0): String {
    val element = Thread.currentThread().stackTrace[depth+4]
    val str = element.fileName
    return "$tag/${element.methodName} ($str:${element.lineNumber})"
}

inline fun <T> T.logv(tag: String="TAG", depth: Int=0,log: (T) -> String): T {
    if (!BuildConfig.DEBUG) return this

    val log = log(this)
    val makeTag = makeTag(tag, depth)
    if (log.isEmpty()) {
        Log.e(makeTag,"CLog message parameter is empty!!")
        return this
    }
    Log.v(makeTag,log)
    return this
}