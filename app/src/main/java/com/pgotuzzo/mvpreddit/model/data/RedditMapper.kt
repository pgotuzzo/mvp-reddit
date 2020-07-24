package com.pgotuzzo.mvpreddit.model.data

import com.pgotuzzo.mvpreddit.model.data.reddit.kojo.RedditPostList
import com.pgotuzzo.mvpreddit.model.entity.Post
import com.pgotuzzo.mvpreddit.util.TimeUtils.SEC_IN_MILLIS
import java.util.Calendar

object RedditMapper {
    fun toPostList(redditPostList: RedditPostList): List<Post> =
        redditPostList.data.children.map {
            with(it.data) {
                val creationDate = Calendar.getInstance()
                    .apply { timeInMillis = createdUtcSecs * SEC_IN_MILLIS }.time
                Post(
                    creationTime = creationDate,
                    id = name,
                    title = title,
                    author = author,
                    thumbnailUrl = thumbnail,
                    commentsCount = commentsCount,
                    isRead = false,
                    isDeleted = false
                )
            }
        }
}