package com.pgotuzzo.mvpreddit.model.data.reddit.kojo

import com.google.gson.annotations.SerializedName
import com.pgotuzzo.mvpreddit.model.data.reddit.kojo.RedditPostListData

data class RedditPostList(
    @SerializedName("kind")
    val kind: String,
    @SerializedName("data")
    val data: RedditPostListData
)