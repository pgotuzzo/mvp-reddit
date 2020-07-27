package com.pgotuzzo.mvpreddit.model.data.post.reddit.kojo

import com.google.gson.annotations.SerializedName

data class RedditPostListData(
    @SerializedName("modhash")
    val modHash: String,
    @SerializedName("dist")
    val dist: Int,
    @SerializedName("children")
    val children: List<RedditPost>,
    @SerializedName("after")
    val after: String?,
    @SerializedName("before")
    val before: String?
)