package com.pgotuzzo.mvpreddit.presentation.feed.listing

import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pgotuzzo.mvpreddit.R
import com.pgotuzzo.mvpreddit.model.entity.Post
import com.pgotuzzo.mvpreddit.presentation.feed.listing.RecyclerAdapter.PostViewHolder
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecyclerAdapter(
    private val onPostsDeleted: (posts: List<Post>) -> Unit,
    private val onPostSelected: (post: Post) -> Unit
) : RecyclerView.Adapter<PostViewHolder>() {

    private val posts: MutableList<Post> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder =
        PostViewHolder(LayoutInflater.from(parent.context), parent)

    override fun getItemCount(): Int = posts.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    fun addPosts(posts: List<Post>) {
        this.posts.addAll(posts)
        notifyItemRangeInserted(itemCount, posts.size)
    }

    /**
     * @param initPos position of the first (fully or partially) visible item
     * @param lastPos position of the last (partially) visible item
     */
    fun removeAll(initPos: Int, lastPos: Int) {
        val oldList = posts.toList()
        // Remove invisible items
        if (initPos > 0) {
            posts.removeAll(oldList.subList(0, initPos))
        }
        posts.removeAll(oldList.subList(lastPos + 1, oldList.size))
        notifyDataSetChanged()
        /*
         * Animate visible items
         * Using OS Handler to let the MainThread deal with the previous changes before attacking
         * the animations
         */
        Handler().post {
            CoroutineScope(Dispatchers.Default).launch {
                repeat(posts.size) {
                    posts.removeAt(0)
                    withContext(Dispatchers.Main) {
                        notifyItemRemoved(0)
                    }
                    delay(100L)
                }
                delay(1000L)
                // Notify about the event
                withContext(Dispatchers.Main) {
                    onPostsDeleted(oldList)
                }
            }
        }
    }

    fun removePost(position: Int) {
        val postDeleted = posts.removeAt(position)
        notifyItemRemoved(position)
        onPostsDeleted(listOf(postDeleted))
    }

    fun setPosts(posts: List<Post>) {
        this.posts.clear()
        this.posts.addAll(posts)
        notifyDataSetChanged()
    }

    inner class PostViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.feed_listing_item, parent, false)) {

        private val ivImage: ImageView = itemView.findViewById(R.id.ivImage)
        private val tvAuthor: TextView = itemView.findViewById(R.id.tvAuthor)
        private val tvCreationTime: TextView = itemView.findViewById(R.id.tvCreationTime)
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvCommentsCount: TextView = itemView.findViewById(R.id.tvCommentsCount)

        fun bind(post: Post) {
            if (!post.thumbnailUrl.isNullOrEmpty()) {
                Picasso.get().load(post.thumbnailUrl).into(ivImage)
            }
            tvAuthor.text = post.author
            tvCreationTime.text = post.creationTimeDesc
            tvTitle.text = post.title
            tvCommentsCount.text = itemView.context.getString(
                R.string.feed_post_comments,
                post.commentsCount.toString()
            )
            itemView.setOnClickListener { onPostSelected(post) }
        }
    }
}