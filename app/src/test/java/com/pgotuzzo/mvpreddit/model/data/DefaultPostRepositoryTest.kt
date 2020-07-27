package com.pgotuzzo.mvpreddit.model.data

import com.pgotuzzo.mvpreddit.model.data.post.DefaultPostRepository
import com.pgotuzzo.mvpreddit.model.data.post.reddit.RedditService
import com.pgotuzzo.mvpreddit.model.data.post.reddit.kojo.RedditPost
import com.pgotuzzo.mvpreddit.model.data.post.reddit.kojo.RedditPostData
import com.pgotuzzo.mvpreddit.model.data.post.reddit.kojo.RedditPostList
import com.pgotuzzo.mvpreddit.model.data.post.reddit.kojo.RedditPostListData
import com.pgotuzzo.mvpreddit.model.data.preferences.Preferences
import com.pgotuzzo.mvpreddit.model.data.preferences.android.Key
import com.pgotuzzo.mvpreddit.util.TimeUtils.SEC_IN_MILLIS
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import kotlin.random.Random

class DefaultPostRepositoryTest {

    private val random: Random = Random(System.currentTimeMillis())

    @MockK(relaxUnitFun = true)
    private lateinit var redditService: RedditService

    @MockK(relaxUnitFun = true)
    private lateinit var preferences: Preferences

    private lateinit var repository: DefaultPostRepository

    @Before
    fun initRepository() {
        MockKAnnotations.init(this)
        repository =
            DefaultPostRepository(
                preferences,
                redditService
            )
    }

    @Test
    fun `get deleted posts IDs list empty`() = runBlockingTest {
        coEvery { preferences.exists(Key.POST_DELETED_LIST) } returns false
        val result = repository.getDeletedPostsIds()
        Assert.assertTrue(result.isEmpty())
    }

    @Test
    fun `get deleted posts IDs list`() = runBlockingTest {
        val deletedIds = arrayOf("1", "2", "3")
        coEvery { preferences.exists(Key.POST_DELETED_LIST) } returns true
        coEvery { preferences.loadStringArray(Key.POST_DELETED_LIST) } returns deletedIds

        val result = repository.getDeletedPostsIds()

        Assert.assertEquals(deletedIds.toList(), result)
    }

    @Test
    fun `save deleted posts IDs for first time`() = runBlockingTest {
        val deletePostIds = listOf("1", "2", "3")
        coEvery { preferences.exists(Key.POST_DELETED_LIST) } returns false

        repository.saveDeletedPostsIds(deletePostIds)

        coVerify { preferences.save(Key.POST_DELETED_LIST, deletePostIds.toTypedArray()) }
    }

    @Test
    fun `save deleted posts IDs overrides existing list`() = runBlockingTest {
        val deletePostIds = listOf("1", "2", "3")
        coEvery { preferences.exists(Key.POST_DELETED_LIST) } returns true
        coEvery { preferences.loadStringArray(Key.POST_DELETED_LIST) } returns arrayOf("storedId")

        repository.saveDeletedPostsIds(deletePostIds)

        coVerify { preferences.save(Key.POST_DELETED_LIST, deletePostIds.toTypedArray()) }
    }

    @Test
    fun `get read posts IDs list empty`() = runBlockingTest {
        coEvery { preferences.exists(Key.POST_READ_LIST) } returns false
        val result = repository.getReadPostsIds()
        Assert.assertTrue(result.isEmpty())
    }

    @Test
    fun `get read posts IDs list`() = runBlockingTest {
        val readPostIds = arrayOf("1", "2", "3")
        coEvery { preferences.exists(Key.POST_READ_LIST) } returns true
        coEvery { preferences.loadStringArray(Key.POST_READ_LIST) } returns readPostIds

        val result = repository.getReadPostsIds()

        Assert.assertEquals(readPostIds.toList(), result)
    }

    @Test
    fun `save read posts IDs for first time`() = runBlockingTest {
        val readPostIds = listOf("1", "2", "3")
        coEvery { preferences.exists(Key.POST_READ_LIST) } returns false

        repository.saveReadPostsIds(readPostIds)

        coVerify { preferences.save(Key.POST_READ_LIST, readPostIds.toTypedArray()) }
    }

    @Test
    fun `save read posts IDs overrides existing list`() = runBlockingTest {
        val readPostIds = listOf("1", "2", "3")
        coEvery { preferences.exists(Key.POST_READ_LIST) } returns true
        coEvery { preferences.loadStringArray(Key.POST_READ_LIST) } returns arrayOf("storedId")

        repository.saveReadPostsIds(readPostIds)

        coVerify { preferences.save(Key.POST_READ_LIST, readPostIds.toTypedArray()) }
    }

    @Test
    fun `get top posts`() = runBlockingTest {
        val amount = random.nextInt(1, 10)
        coEvery { redditService.getPosts(amount, null) } returns createRedditPostList(amount)

        val result = repository.getTopPosts(amount)

        Assert.assertEquals(amount, result.size)
    }

    @Test
    fun `get more posts`() = runBlockingTest {
        val afterPostId = random.nextLong().toString()
        val amount = random.nextInt(1, 10)
        coEvery { redditService.getPosts(amount, afterPostId) } returns createRedditPostList(amount)

        val result = repository.getMorePosts(afterPostId, amount)

        Assert.assertEquals(amount, result.size)
    }

    private fun createRedditPostList(postAmount: Int): RedditPostList =
        RedditPostList(
            kind = "kind",
            data = RedditPostListData(
                modHash = "",
                dist = postAmount,
                children = (1..postAmount).map { createRedditPost() },
                after = null,
                before = null
            )
        )

    private fun createRedditPost(): RedditPost =
        RedditPost(
            kind = "kind",
            data = RedditPostData(
                name = random.nextLong().toString(),
                author = "me",
                title = "I'm a title",
                createdUtcSecs = randomCreationUTCinSec(),
                thumbnail = null,
                url = null,
                commentsCount = random.nextLong()
            )
        )

    private fun randomCreationUTCinSec(): Long =
        (System.currentTimeMillis() - random.nextLong(10000000)) / SEC_IN_MILLIS
}