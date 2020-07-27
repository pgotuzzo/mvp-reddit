package com.pgotuzzo.mvpreddit.model.data.post

import com.pgotuzzo.mvpreddit.model.data.post.reddit.kojo.RedditPostList
import com.pgotuzzo.mvpreddit.model.entity.Post
import com.pgotuzzo.mvpreddit.util.TimeUtils.SEC_IN_MILLIS
import java.util.Calendar

// FIXME - Add tests
object RedditMapper {

    fun toPostList(redditPostList: RedditPostList): List<Post> =
        redditPostList.data.children.map {
            with(it.data) {
                val creationDate = Calendar.getInstance()
                    .apply { timeInMillis = createdUtcSecs * SEC_IN_MILLIS }.time
                val imageUrl = url?.run {
                    if (endsWith(".png") || endsWith(".jpg")) this
                    else null
                }
                Post(
                    creationTime = creationDate,
                    id = name,
                    title = title,
                    author = author,
                    thumbnailUrl = thumbnail,
                    imageUrl = imageUrl,
                    commentsCount = commentsCount,
                    isRead = false,
                    isDeleted = false
                )
            }
        }
}