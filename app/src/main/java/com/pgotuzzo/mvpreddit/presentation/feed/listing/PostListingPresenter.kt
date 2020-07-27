package com.pgotuzzo.mvpreddit.presentation.feed.listing

import com.pgotuzzo.mvpreddit.model.data.ActivityStateRepository
import com.pgotuzzo.mvpreddit.model.domain.PostService
import com.pgotuzzo.mvpreddit.model.entity.Post
import com.pgotuzzo.mvpreddit.presentation.base.BasePresenter
import com.pgotuzzo.mvpreddit.presentation.base.LoggerHolder
import com.pgotuzzo.mvpreddit.presentation.feed.listing.PostListingContract.Presenter
import com.pgotuzzo.mvpreddit.presentation.feed.listing.PostListingContract.View
import com.pgotuzzo.mvpreddit.util.log.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PostListingPresenter(
    logger: Logger,
    repo: ActivityStateRepository,
    private val postService: PostService
) : BasePresenter<View, PostListingState>(
    LoggerHolder(logger, Tag.CLASS),
    repo,
    PostListingState.INITIAL_STATE
), Presenter {

    companion object {
        private const val PAGE_SIZE = 20
    }

    private object Tag {
        val CLASS = PostListingPresenter::class.java.simpleName
    }

    private var posts: List<Post> = emptyList()
    private var loading: Boolean = false

    // BasePresenter implementation - BEGIN
    override fun applyState(state: PostListingState?) {
        state?.also { posts = it.posts }
    }

    override fun getCurrentState(): PostListingState? = PostListingState(posts)
    // BasePresenter implementation - END

    override fun onInit() {
        logger.d(Tag.CLASS, "Initialize")
        if (posts.isEmpty()) {
            loadPosts()
        } else {
            view?.showPosts(posts)
        }
    }

    override fun onPostsRead(post: Post) {
        logger.d(Tag.CLASS, "Post read | Post: ${post.id}")
        val job = CoroutineScope(Dispatchers.Main).launch {
            postService.markAsRead(post.id, true)
        }
        addJobInProgress(job)
    }

    override fun onPostsRemoved(posts: List<Post>) {
        logger.d(Tag.CLASS, "${posts.size} posts removed")
        val job = CoroutineScope(Dispatchers.Main).launch {
            posts.forEach { postService.deletePost(it.id) }
        }
        addJobInProgress(job)
        loading = false
        this.posts = this.posts.minus(posts)
        if (this.posts.isEmpty()) {
            logger.d(Tag.CLASS, "Empty list. Load new posts")
            loadPosts()
        }
    }

    override fun onRemoveAllClick() {
        logger.d(Tag.CLASS, "Remove all click. Try to refresh")
        refresh()
    }

    override fun onScrollEnd() {
        if (!loading) {
            loading = true
            CoroutineScope(Dispatchers.Main).launch {
                val newPosts = postService.getMorePosts(posts.last().id, PAGE_SIZE)
                posts = posts.plus(newPosts)
                view?.addPosts(newPosts)
                loading = false
            }
        }
    }

    override fun onSwipeToRefresh() {
        logger.d(Tag.CLASS, "Swipe to the top. Try to refresh")
        refresh()
    }

    private fun loadPosts() {
        val job = CoroutineScope(Dispatchers.Main).launch {
            posts = postService.getTopPosts(PAGE_SIZE)
            view?.showPosts(posts)
        }
        addJobInProgress(job)
    }

    private fun refresh() {
        if (!loading) {
            logger.d(Tag.CLASS, "Refreshing")
            loading = true
            view?.removeAll()
        } else {
            logger.d(Tag.CLASS, "Loading yet. Aborting refresh")
        }
    }
}