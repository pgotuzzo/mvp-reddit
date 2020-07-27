package com.pgotuzzo.mvpreddit.model.domain

import android.text.format.DateUtils.SECOND_IN_MILLIS
import com.pgotuzzo.mvpreddit.model.data.post.PostRepository
import com.pgotuzzo.mvpreddit.model.entity.Post
import com.pgotuzzo.mvpreddit.util.TimeUtils.HOUR_IN_MILLIS
import com.pgotuzzo.mvpreddit.util.TimeUtils.MIN_IN_MILLIS
import com.pgotuzzo.mvpreddit.util.log.Logger
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import java.util.Calendar
import kotlin.random.Random

class DefaultPostServiceTest {

    private val calendar = Calendar.getInstance()
    private val random = Random(System.currentTimeMillis())
    private val topPosts: List<Post> = listOf(
        createPost(10L * SECOND_IN_MILLIS, "1"),
        createPost(2L * MIN_IN_MILLIS, "2"),
        createPost(5L * MIN_IN_MILLIS, "3"),
        createPost(30L * MIN_IN_MILLIS, "4"),
        createPost(1L * HOUR_IN_MILLIS, "5"),
        createPost(10L * HOUR_IN_MILLIS, "6")
    )

    @MockK(relaxUnitFun = true)
    private lateinit var logger: Logger

    @MockK(relaxUnitFun = true)
    private lateinit var repository: PostRepository

    private lateinit var service: DefaultPostService

    @Before
    fun initService() {
        MockKAnnotations.init(this)
        service = DefaultPostService(repository, logger)
    }

    @Test
    fun `post deleted is attached to existing list`() = runBlockingTest {
        val deletedList = listOf("1", "2", "3")
        val postId = "postIdToDelete"
        coEvery { repository.getDeletedPostsIds() } returns deletedList

        service.deletePost(postId)

        coVerify { repository.saveDeletedPostsIds(deletedList.plus(postId)) }
    }

    @Test
    fun `post read is attached to existing list`() = runBlockingTest {
        val readList = listOf("1", "2", "3")
        val postId = "postIdRead"
        coEvery { repository.getReadPostsIds() } returns readList

        service.markAsRead(postId, true)

        coVerify { repository.saveReadPostsIds(readList.plus(postId)) }
    }

    @Test
    fun `post unread is removed from existing list`() = runBlockingTest {
        val readList = listOf("1", "2", "3")
        val postId = readList.random()
        coEvery { repository.getReadPostsIds() } returns readList

        service.markAsRead(postId, false)

        coVerify { repository.saveReadPostsIds(readList.minus(postId)) }
    }

    @Test
    fun `get top posts returns expected amount`() = runBlockingTest {
        val size = random.nextInt(1, topPosts.size)
        coEvery { repository.getTopPosts(size) } returns topPosts.subList(0, size)
        coEvery { repository.getDeletedPostsIds() } returns emptyList()
        coEvery { repository.getReadPostsIds() } returns emptyList()

        Assert.assertEquals(size, service.getTopPosts(size).size)
    }

    @Test
    fun `get top posts filters deleted and returns expected amount`() = runBlockingTest {
        val size = random.nextInt(1, topPosts.size)
        val topPostRandom = topPosts.subList(0, size)
        val deleted = topPostRandom.random().id
        val additionalPosts = listOf(createPost(100, "extra"))
        coEvery { repository.getTopPosts(size) } returns topPostRandom
        coEvery { repository.getDeletedPostsIds() } returns listOf(deleted)
        coEvery { repository.getReadPostsIds() } returns emptyList()
        coEvery { repository.getMorePosts(topPostRandom.last().id, 1) } returns additionalPosts

        val result = service.getTopPosts(size)

        Assert.assertTrue(result.size == size)
        Assert.assertFalse(result.map { it.id }.contains(deleted))
    }

    @Test
    fun `get more posts returns expected amount`() = runBlockingTest {
        val amount = random.nextInt(1, topPosts.size - 1)
        val morePostRandom = topPosts.subList(1, amount + 1)
        val after = topPosts.first().id
        coEvery { repository.getMorePosts(after, amount) } returns morePostRandom
        coEvery { repository.getDeletedPostsIds() } returns emptyList()

        val result = service.getMorePosts(after, amount)

        Assert.assertTrue(result.size == amount)
    }

    private fun createPost(beforeInMillis: Long, id: String): Post {
        calendar.apply { timeInMillis = System.currentTimeMillis() - beforeInMillis }
        return Post(
            creationTime = calendar.time,
            id = id,
            author = "me",
            title = "I'm a title",
            thumbnailUrl = null,
            imageUrl = null,
            commentsCount = 0,
            isRead = false,
            isDeleted = false
        )
    }
}