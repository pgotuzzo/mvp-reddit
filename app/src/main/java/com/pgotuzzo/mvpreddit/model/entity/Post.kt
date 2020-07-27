package com.pgotuzzo.mvpreddit.model.entity

import android.os.Parcelable
import com.pgotuzzo.mvpreddit.util.TimeUtils.HOUR_IN_MILLIS
import com.pgotuzzo.mvpreddit.util.TimeUtils.MIN_IN_MILLIS
import com.pgotuzzo.mvpreddit.util.TimeUtils.SEC_IN_MILLIS
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
class Post(
    private val creationTime: Date,
    val id: String,
    val author: String,
    val title: String,
    val thumbnailUrl: String?,
    val imageUrl: String?,
    val commentsCount: Long,
    var isRead: Boolean,
    var isDeleted: Boolean
) : Parcelable {

    val creationTimeDesc: String
        get() {
            val delta = System.currentTimeMillis() - creationTime.time
            val hours = delta / HOUR_IN_MILLIS
            val mins = delta / MIN_IN_MILLIS
            val secs = delta / SEC_IN_MILLIS
            return when {
                hours > 0 -> "$hours hour(s) ago"
                mins > 0 -> "$mins minute(s) ago"
                else -> "$secs second(s) ago"
            }
        }
}