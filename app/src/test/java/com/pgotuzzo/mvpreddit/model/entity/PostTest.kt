package com.pgotuzzo.mvpreddit.model.entity

import com.pgotuzzo.mvpreddit.util.TimeUtils.HOUR_IN_MILLIS
import com.pgotuzzo.mvpreddit.util.TimeUtils.MIN_IN_MILLIS
import com.pgotuzzo.mvpreddit.util.TimeUtils.SEC_IN_MILLIS
import org.junit.*
import java.util.Calendar
import java.util.Date

class PostTest {

    @Test
    fun `creation time description based on hours`() {
        val anHourAgoDate = Calendar.getInstance()
            .apply { timeInMillis = System.currentTimeMillis() - HOUR_IN_MILLIS }
            .time
        val post = createPost(anHourAgoDate)
        Assert.assertEquals("1 hour(s) ago", post.creationTimeDesc)
    }

    @Test
    fun `creation time description based on minutes`() {
        val aMinuteAgoDate = Calendar.getInstance()
            .apply { timeInMillis = System.currentTimeMillis() - MIN_IN_MILLIS }
            .time
        val post = createPost(aMinuteAgoDate)
        Assert.assertEquals("1 minute(s) ago", post.creationTimeDesc)
    }

    @Test
    fun `creation time description based on seconds`() {
        val anSecondAgoDate = Calendar.getInstance()
            .apply { timeInMillis = System.currentTimeMillis() - SEC_IN_MILLIS }
            .time
        val post = createPost(anSecondAgoDate)
        Assert.assertEquals("1 second(s) ago", post.creationTimeDesc)
    }

    private fun createPost(creationTimeDate: Date): Post =
        Post(
            creationTime = creationTimeDate,
            id = "0",
            author = "me",
            title = "I'm a title",
            thumbnailUrl = null,
            commentsCount = 0,
            isRead = false,
            isDeleted = false
        )
}