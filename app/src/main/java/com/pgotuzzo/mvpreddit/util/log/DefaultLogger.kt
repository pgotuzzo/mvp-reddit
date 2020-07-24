package com.pgotuzzo.mvpreddit.util.log

import android.util.Log

/**
 * [Logger] implementation base on default Android util for logging
 */
class DefaultLogger : Logger {

    override fun v(tag: String, msg: String) {
        Log.v(tag, msg)
    }

    override fun d(tag: String, msg: String) {
        Log.d(tag, msg)
    }

    override fun i(tag: String, msg: String) {
        Log.i(tag, msg)
    }

    override fun w(tag: String, msg: String) {
        Log.w(tag, msg)
    }

    override fun e(tag: String, e: Exception, msg: String) {
        Log.e(tag, msg, e)
    }
}