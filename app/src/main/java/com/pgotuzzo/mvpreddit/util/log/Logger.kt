package com.pgotuzzo.mvpreddit.util.log

/**
 * Levels hierarchy:
 * Verbose < Debug < Info < Warning < Error
 */
interface Logger {

    /**
     * Log level: Debug
     */
    fun d(tag: String, msg: String)

    /**
     * Log level: Error
     */
    fun e(tag: String, e: Exception, msg: String = "")

    /**
     * Log level: Info
     */
    fun i(tag: String, msg: String)

    /**
     * Log level: Verbose
     */
    fun v(tag: String, msg: String)

    /**
     * Log level: Warning
     */
    fun w(tag: String, msg: String)
}