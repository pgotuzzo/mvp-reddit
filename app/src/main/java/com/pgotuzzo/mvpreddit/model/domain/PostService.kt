package com.pgotuzzo.mvpreddit.model.domain

import com.pgotuzzo.mvpreddit.model.entity.Post

interface PostService {

    /**
     * Deletes a post, so that it won't be included while requesting posts in the future
     * @see getTopPosts
     * @see getMorePosts
     *
     * @param postId id of the post to be deleted
     */
    suspend fun deletePost(postId: String)

    /**
     * Fetches the following (= older) [amount] number of posts, starting from (not included)
     * the post which id is [afterPostId]
     *
     * @param afterPostId post id used as anchor
     * @param amount exact amount of post to be fetched
     * @return post list with [amount] size
     */
    suspend fun getMorePosts(afterPostId: String, amount: Int): List<Post>

    /**
     * Fetches the top [amount] number of posts
     *
     * @param amount exact amount of post to be fetched
     * @return post list with [amount] size
     */
    suspend fun getTopPosts(amount: Int): List<Post>

    /**
     * Marks a post as read/unread. Persists this state for future calls to
     * @see getTopPosts
     * @see getMorePosts
     *
     * @param postId id of the post which state has to be saved
     * @param isRead true if the post has to be marked as read
     */
    suspend fun markAsRead(postId: String, isRead: Boolean)
}