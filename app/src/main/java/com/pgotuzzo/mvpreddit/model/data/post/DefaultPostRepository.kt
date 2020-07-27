package com.pgotuzzo.mvpreddit.model.data.post

import com.pgotuzzo.mvpreddit.model.data.post.reddit.RedditService
import com.pgotuzzo.mvpreddit.model.data.preferences.Preferences
import com.pgotuzzo.mvpreddit.model.data.preferences.android.Key
import com.pgotuzzo.mvpreddit.model.entity.Post
import kotlinx.coroutines.Dispatchers

class DefaultPostRepository(
    private val preferences: Preferences,
    private val redditService: RedditService
) : PostRepository {

    override suspend fun getDeletedPostsIds(): List<String> =
        if (preferences.exists(Key.POST_DELETED_LIST))
            preferences.loadStringArray(Key.POST_DELETED_LIST).toList()
        else
            emptyList()

    override suspend fun getMorePosts(afterPostId: String, amount: Int): List<Post> =
        RedditMapper.toPostList(
            with(Dispatchers.IO) {
                redditService.getPosts(amount, afterPostId)
            }

        )

    override suspend fun getReadPostsIds(): List<String> =
        if (preferences.exists(Key.POST_READ_LIST))
            preferences.loadStringArray(Key.POST_READ_LIST).toList()
        else
            emptyList()

    override suspend fun getTopPosts(amount: Int): List<Post> =
        RedditMapper.toPostList(
            with(Dispatchers.IO) {
                redditService.getPosts(amount, null)
            }
        )

    override suspend fun saveDeletedPostsIds(postsIds: List<String>) {
        preferences.save(Key.POST_DELETED_LIST, postsIds.toTypedArray())
    }

    override suspend fun saveReadPostsIds(postsIds: List<String>) {
        preferences.save(Key.POST_READ_LIST, postsIds.toTypedArray())
    }
}