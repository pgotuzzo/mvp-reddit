package com.pgotuzzo.mvpreddit.model.data.reddit.kojo

import com.google.gson.annotations.SerializedName

data class RedditPostData(
    @SerializedName("name")
    val name: String,
    @SerializedName("author")
    val author: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("created_utc")
    val createdUtcSecs: Long,
    @SerializedName("thumbnail")
    val thumbnail: String?,
    @SerializedName("num_comments")
    val commentsCount: Long,
    @SerializedName("url")
    val url: String?
)