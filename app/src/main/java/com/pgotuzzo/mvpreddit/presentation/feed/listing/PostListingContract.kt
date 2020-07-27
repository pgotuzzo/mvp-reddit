package com.pgotuzzo.mvpreddit.presentation.feed.listing

import com.pgotuzzo.mvpreddit.model.entity.Post
import com.pgotuzzo.mvpreddit.presentation.MvpPresenter
import com.pgotuzzo.mvpreddit.presentation.MvpView

interface PostListingContract {
    interface View : MvpView {
        /**
         * Adds new posts to the existing list
         * @param posts list of posts to be added
         */
        fun addPosts(posts: List<Post>)

        /**
         * Removes all the posts in the list
         */
        fun removeAll()

        /**
         * Replaces existing (if any) posts in the list by [posts]
         * @param posts list of post to be shown
         */
        fun showPosts(posts: List<Post>)
    }

    interface Presenter : MvpPresenter<View> {
        /**
         * View is ready for initialization
         */
        fun onInit()

        /**
         * Posts has been read
         * @param post post read
         */
        fun onPostsRead(post: Post)

        /**
         * Posts removed from the list view (animation completed)
         * @param posts list of posts removed
         */
        fun onPostsRemoved(posts: List<Post>)

        /**
         * Remove all request made
         */
        fun onRemoveAllClick()

        /**
         * Bottom of the list has been reached
         */
        fun onScrollEnd()

        /**
         * Remove all request made
         */
        fun onSwipeToRefresh()
    }
}