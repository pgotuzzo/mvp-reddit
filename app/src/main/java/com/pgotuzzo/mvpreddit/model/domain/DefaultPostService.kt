package com.pgotuzzo.mvpreddit.model.domain

import com.pgotuzzo.mvpreddit.model.data.post.PostRepository
import com.pgotuzzo.mvpreddit.model.entity.Post
import com.pgotuzzo.mvpreddit.util.log.Logger

class DefaultPostService(
    private val postRepository: PostRepository,
    private val logger: Logger
) : PostService {

    companion object {
        val TAG: String = DefaultPostService::class.java.simpleName
    }

    override suspend fun deletePost(postId: String) {
        val deletedPosts = postRepository.getDeletedPostsIds()
        if (deletedPosts.contains(postId)) {
            logger.d(TAG, "Post already deleted. No change")
        } else {
            postRepository.saveDeletedPostsIds(deletedPosts.plus(postId))
        }
    }

    override suspend fun getMorePosts(afterPostId: String, amount: Int): List<Post> =
        getPosts(afterPostId, amount, emptyList(), postRepository.getDeletedPostsIds())

    override suspend fun getTopPosts(amount: Int): List<Post> {
        val deletedPosts = postRepository.getDeletedPostsIds()
        val readPosts = postRepository.getReadPostsIds()
        val topPosts = postRepository.getTopPosts(amount)
        val filtered = topPosts.filter { !deletedPosts.contains(it.id) }
        return getPosts(topPosts.last().id, amount, filtered, deletedPosts).map {
            if (readPosts.contains(it.id)) it.isRead = true
            it
        }
    }

    override suspend fun markAsRead(postId: String, isRead: Boolean) {
        val readPosts = postRepository.getReadPostsIds()
        when {
            readPosts.contains(postId) && !isRead -> {
                logger.d(TAG, "Removing post: $postId from posts read list")
                postRepository.saveReadPostsIds(readPosts.minus(postId))
            }
            !readPosts.contains(postId) && isRead -> {
                logger.d(TAG, "Adding post: $postId to posts read list")
                postRepository.saveReadPostsIds(readPosts.plus(postId))
            }
            else -> {
                logger.d(TAG, "No change required")
            }
        }
    }

    private suspend fun getPosts(
        afterPostId: String,
        amount: Int,
        posts: List<Post>,
        deletedPosts: List<String>
    ): List<Post> =
        if (posts.size == amount) {
            posts
        } else {
            // Still missing post to be fetched
            val missingAmount = amount - posts.size
            // New posts fetched - Not filtered
            val morePost = postRepository.getMorePosts(afterPostId, missingAmount)
            // Total posts fetched - Filtered (removing deleted ones)
            val nextIterationPosts = posts.plus(morePost.filter { !deletedPosts.contains(it.id) })
            getPosts(morePost.last().id, amount, nextIterationPosts, deletedPosts)
        }
}