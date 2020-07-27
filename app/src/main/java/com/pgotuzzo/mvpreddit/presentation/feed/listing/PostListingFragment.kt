package com.pgotuzzo.mvpreddit.presentation.feed.listing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.pgotuzzo.mvpreddit.R
import com.pgotuzzo.mvpreddit.model.entity.Post
import com.pgotuzzo.mvpreddit.presentation.feed.FeedActivity
import com.pgotuzzo.mvpreddit.util.TimeUtils
import kotlinx.android.synthetic.main.frag_feed_post_listing.fabDeleteAll
import kotlinx.android.synthetic.main.frag_feed_post_listing.recycler
import kotlinx.android.synthetic.main.frag_feed_post_listing.swiperefresh
import java.util.Calendar
import java.util.Random

/**
 * TODO(
 *   01. Documentation
 *   02. Dagger Injection
 *   03. Logs
 *  )
 */
class PostListingFragment : Fragment() {

    companion object {

        fun getInstance(): PostListingFragment = PostListingFragment()
    }

    private var adapter: RecyclerAdapter = RecyclerAdapter(
        { onPostsRemoved() },
        { (activity as FeedActivity).showDetails(it) }
    )
    private val layoutManager = LinearLayoutManager(context).apply { orientation = VERTICAL }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.frag_feed_post_listing, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Recycler
        initRecycler()
        // Swipe to refresh
        swiperefresh.setOnRefreshListener { refresh() }
        // Remove all button
        fabDeleteAll.setOnClickListener { refresh() }
        adapter.setPosts((1..20).map { createPost() })
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
        ItemTouchHelper(ItemTouchCallback(context!!) { pos -> adapter.removePost(pos) })
            .attachToRecyclerView(recycler)
        // Endless Scroll
        recycler.addOnScrollListener(VerticalScrollEndListener(layoutManager) { onScrollEnd() })
    }

    private fun onPostsRemoved() {
        if (swiperefresh.isRefreshing) {
            adapter.setPosts((1..20).map { createPost() })
            swiperefresh.isRefreshing = false
        }
    }

    private fun onScrollEnd() {
        if (!swiperefresh.isRefreshing) {
            adapter.addPosts((1..20).map { createPost() })
        }
    }

    private fun refresh() {
        swiperefresh.isRefreshing = true
        adapter.removeAll(
            layoutManager.findFirstVisibleItemPosition(),
            layoutManager.findLastVisibleItemPosition()
        )
    }

    // TODO - Remove
    private val random = Random(System.currentTimeMillis())
    private val userNames = listOf(
        "PabloG",
        "ToTox",
        "TheBigBangTheory",
        "NarUto",
        "User321",
        "___KATo___",
        "xH1999",
        "Ellon_Musk"
    )
    private val titles = listOf(
        "I'm a very short title",
        "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s",
        "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum"
    )
    private val images = listOf(
        "https://picsum.photos/201/301",
        "https://picsum.photos/202/302",
        "https://picsum.photos/203/303",
        "https://picsum.photos/204/305",
        "https://picsum.photos/205/106",
        "https://picsum.photos/206/107",
        "https://picsum.photos/207/108",
        "https://picsum.photos/208/109",
        ""
    )

    private fun createPost(): Post {
        val creationDate = Calendar.getInstance()
            .apply { timeInMillis = System.currentTimeMillis() - TimeUtils.HOUR_IN_MILLIS }.time
        val image = images.random()
        return Post(
            creationTime = creationDate,
            id = random.nextLong().toString(),
            author = userNames.random(),
            title = titles.random(),
            thumbnailUrl = image,
            imageUrl = image,
            commentsCount = random.nextLong() % 1000000,
            isRead = false,
            isDeleted = false
        )
    }
}