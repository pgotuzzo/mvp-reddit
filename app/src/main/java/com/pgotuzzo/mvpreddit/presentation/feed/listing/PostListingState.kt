package com.pgotuzzo.mvpreddit.presentation.feed.listing

import com.pgotuzzo.mvpreddit.model.entity.Post
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PostListingState(override val posts: List<Post>) : PostListingContract.State {

    companion object {
        val INITIAL_STATE = PostListingState(emptyList())
    }
}