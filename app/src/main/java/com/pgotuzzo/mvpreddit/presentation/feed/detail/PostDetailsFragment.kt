package com.pgotuzzo.mvpreddit.presentation.feed.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pgotuzzo.mvpreddit.R
import com.pgotuzzo.mvpreddit.model.entity.Post
import com.pgotuzzo.mvpreddit.presentation.exception.InvalidArgumentsException
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.frag_feed_post_details.ivImage
import kotlinx.android.synthetic.main.frag_feed_post_details.tvAuthor
import kotlinx.android.synthetic.main.frag_feed_post_details.tvCommentsCount
import kotlinx.android.synthetic.main.frag_feed_post_details.tvCreationTime
import kotlinx.android.synthetic.main.frag_feed_post_details.tvTitle

/*
 * TODO(
 *   01. Documentation
 *   02. Dagger Injection
 *   03. Logs
 *  )
 */
class PostDetailsFragment : Fragment() {

    companion object {
        private const val ARGS_POST = "POST"

        fun getInstance(post: Post): PostDetailsFragment =
            PostDetailsFragment().apply {
                arguments = Bundle().apply { putParcelable(ARGS_POST, post) }
            }
    }

    lateinit var post: Post

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        post = arguments?.getParcelable(ARGS_POST) as? Post
            ?: throw InvalidArgumentsException("Post Details Fragment arguments missing")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.frag_feed_post_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvAuthor.text = post.author
        tvTitle.text = post.title
        tvCommentsCount.text =
            getString(R.string.feed_post_comments, post.commentsCount.toString())
        tvCreationTime.text = post.creationTimeDesc

        post.imageUrl?.also {
            if (it.isNotEmpty()) {
                ivImage.visibility = VISIBLE
                Picasso.get().load(it).into(ivImage)
            }
        }
    }
}