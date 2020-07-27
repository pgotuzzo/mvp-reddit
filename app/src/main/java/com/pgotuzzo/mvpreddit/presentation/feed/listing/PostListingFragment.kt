package com.pgotuzzo.mvpreddit.presentation.feed.listing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.pgotuzzo.mvpreddit.R
import com.pgotuzzo.mvpreddit.model.entity.Post
import com.pgotuzzo.mvpreddit.presentation.base.BaseFragment
import com.pgotuzzo.mvpreddit.presentation.feed.FeedActivity
import com.pgotuzzo.mvpreddit.presentation.feed.listing.di.DaggerPostListingComponent
import com.pgotuzzo.mvpreddit.presentation.feed.listing.di.PostListingModule
import com.pgotuzzo.mvpreddit.presentation.feed.listing.recycler.ItemAnimator
import com.pgotuzzo.mvpreddit.presentation.feed.listing.recycler.ItemTouchCallback
import com.pgotuzzo.mvpreddit.presentation.feed.listing.recycler.RecyclerAdapter
import com.pgotuzzo.mvpreddit.presentation.feed.listing.recycler.VerticalScrollEndListener
import com.pgotuzzo.mvpreddit.presentation.ktx.applicationComponent
import kotlinx.android.synthetic.main.frag_feed_post_listing.fabDeleteAll
import kotlinx.android.synthetic.main.frag_feed_post_listing.recycler
import kotlinx.android.synthetic.main.frag_feed_post_listing.swiperefresh
import javax.inject.Inject

/**
 * Show posts in a endless scroll view
 * Supports single post removal, dynamic fetch, and all feed refresh
 */
class PostListingFragment :
    BaseFragment<PostListingContract.View, PostListingContract.Presenter>(Tag.CLASS),
    PostListingContract.View {

    companion object {

        fun getInstance(): PostListingFragment = PostListingFragment()
    }

    private object Tag {
        val CLASS: String = PostListingFragment::class.java.simpleName
    }

    @Inject
    override lateinit var presenter: PostListingContract.Presenter

    override val view: PostListingContract.View = this

    private val adapter = RecyclerAdapter({ onPostsRemoved(it) }, { onPostClicked(it) })
    private val layoutManager = LinearLayoutManager(context).apply { orientation = VERTICAL }

    override fun inject() {
        DaggerPostListingComponent.builder()
            .applicationComponent(applicationComponent)
            .postListingModule(PostListingModule(activity!!))
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.frag_feed_post_listing, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Recycler
        initRecycler()
        // Swipe to refresh
        swiperefresh.setOnRefreshListener { presenter.onSwipeToRefresh() }
        // Remove all button
        fabDeleteAll.setOnClickListener { presenter.onRemoveAllClick() }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.onInit()
    }

    override fun addPosts(posts: List<Post>) {
        adapter.addPosts(posts)
    }

    override fun removeAll() {
        refresh()
    }

    override fun showPosts(posts: List<Post>) {
        swiperefresh.isRefreshing = false
        adapter.setPosts(posts)
    }

    private fun initRecycler() {
        recycler.adapter = adapter
        recycler.layoutManager = layoutManager
        /*
         * Items deletion - Animations
         *  - Swipe left to dismiss (manual removal)
         *  - Automatic removal animation (automatic removal)
         */
        recycler.itemAnimator = ItemAnimator()
        ItemTouchHelper(
            ItemTouchCallback(context!!) { pos ->
                logger.d(Tag.CLASS, "Post deleted by swipe | Position: $pos")
                adapter.removePost(pos)
            }
        ).attachToRecyclerView(recycler)
        // Endless Scroll
        recycler.addOnScrollListener(VerticalScrollEndListener(layoutManager) { onScrollEnd() })
    }

    private fun onPostClicked(post: Post) {
        /*
         * @pgotuzzo Improvement - Run out of time
         * The ideal way of doing this, in my opinion, is wiring the presenter with the FeedActivity
         * Presenter, by the usage of a Coroutine Channel (kind of Event Bus). I couldn't work on
         * that. Leaving as it is ... :(
         */
        logger.d(Tag.CLASS, "Post selected | Post: $post")
        presenter.onPostsRead(post)
        (activity as FeedActivity).showDetails(post)
    }

    private fun onPostsRemoved(posts: List<Post>) {
        presenter.onPostsRemoved(posts)
    }

    private fun onScrollEnd() {
        if (!swiperefresh.isRefreshing) {
            logger.d(Tag.CLASS, "Scroll end reached. Notify presenter")
            presenter.onScrollEnd()
        }
    }

    private fun refresh() {
        logger.d(Tag.CLASS, "Refresh list")
        swiperefresh.isRefreshing = true
        adapter.removeAll(
            layoutManager.findFirstVisibleItemPosition(),
            layoutManager.findLastVisibleItemPosition()
        )
    }
}