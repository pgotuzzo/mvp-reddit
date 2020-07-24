package com.pgotuzzo.mvpreddit.model.data.reddit

import com.pgotuzzo.mvpreddit.model.data.reddit.kojo.RedditPostList
import retrofit2.http.GET
import retrofit2.http.Query

interface RedditService {

    @GET("top.json")
    suspend fun getPosts(
        @Query("limit") limit: Int,
        @Query("after") afterPostId: String?
    ): RedditPostList
}