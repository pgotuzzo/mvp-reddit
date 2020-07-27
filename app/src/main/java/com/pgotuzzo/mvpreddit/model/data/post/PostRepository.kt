package com.pgotuzzo.mvpreddit.model.data.post

import com.pgotuzzo.mvpreddit.model.entity.Post

interface PostRepository {

    /**
     * Provides the id list of the deleted posts
     *
     * @return list of post ids
     */
    suspend fun getDeletedPostsIds(): List<String>

    /**
     * Provides the following (== older) [amount] number of posts, starting from (not included)
     * the post which id is [afterPostId]
     *
     * @param afterPostId post id used as anchor
     * @param amount exact amount of post to be provided with
     * @return post list with [amount] size
     */
    suspend fun getMorePosts(afterPostId: String, amount: Int): List<Post>

    /**
     * Provides the id list of the read posts
     *
     * @return list of post ids
     */
    suspend fun getReadPostsIds(): List<String>

    /**
     * Provides the top [amount] number of posts
     *
     * @param amount exact amount of post to be provided with
     * @return post list with [amount] size
     */
    suspend fun getTopPosts(amount: Int): List<Post>

    /**
     * Replaces the current id list of deleted posts with [postsIds]
     *
     * @param postsIds id list of deleted posts
     */
    suspend fun saveDeletedPostsIds(postsIds: List<String>)

    /**
     * Replaces the current id list of read posts with [postsIds]
     *
     * @param postsIds id list of read posts
     */
    suspend fun saveReadPostsIds(postsIds: List<String>)
}