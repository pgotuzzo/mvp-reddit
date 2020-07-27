package com.pgotuzzo.mvpreddit.presentation.feed

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pgotuzzo.mvpreddit.R
import com.pgotuzzo.mvpreddit.model.entity.Post
import com.pgotuzzo.mvpreddit.presentation.feed.FeedActivity.FragmentTag.POST_DETAIL
import com.pgotuzzo.mvpreddit.presentation.feed.FeedActivity.FragmentTag.POST_LISTING
import com.pgotuzzo.mvpreddit.presentation.feed.detail.PostDetailsFragment
import com.pgotuzzo.mvpreddit.presentation.feed.listing.PostListingFragment
import kotlinx.android.synthetic.main.act_feed.flDeatils
import kotlinx.android.synthetic.main.act_feed.flListing

class FeedActivity : AppCompatActivity() {

    object FragmentTag {
        const val POST_DETAIL = "FRAG_FEED_POST_DETAIL"
        const val POST_LISTING = "FRAG_FEED_POST_LISTING"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_feed)
        if (savedInstanceState == null) {
            showListing()
        }
    }

    fun showDetails(post: Post) {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        }
        val fragment = PostDetailsFragment.getInstance(post).apply { retainInstance = true }
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.animator.frament_enter,
                R.animator.frament_exit,
                R.animator.frament_enter,
                R.animator.frament_exit
            )
            .add(flDeatils.id, fragment, POST_DETAIL)
            .addToBackStack(null)
            .commit()
    }

    private fun showListing() {
        supportFragmentManager
            .beginTransaction()
            .add(flListing.id, PostListingFragment.getInstance(), POST_LISTING)
            .commit()
    }
}