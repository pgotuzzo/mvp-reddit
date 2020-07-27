package com.pgotuzzo.mvpreddit.model.data.post.reddit.kojo

import com.google.gson.annotations.SerializedName

data class RedditPost(
    @SerializedName("kind")
    val kind: String,
    @SerializedName("data")
    val data: RedditPostData
)