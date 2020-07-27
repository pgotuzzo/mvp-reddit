package com.pgotuzzo.mvpreddit.model.data.post.reddit.kojo

import com.google.gson.annotations.SerializedName

data class RedditPostList(
    @SerializedName("kind")
    val kind: String,
    @SerializedName("data")
    val data: RedditPostListData
)