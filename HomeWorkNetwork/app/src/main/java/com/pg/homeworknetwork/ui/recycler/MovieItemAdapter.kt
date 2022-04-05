package com.pg.homeworknetwork.ui.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.pg.homeworknetwork.BuildConfig
import com.pg.homeworknetwork.data.Movies
import com.pg.homeworknetwork.R

internal class MovieItemAdapter : ListAdapter<Movies.Short, MovieItemAdapter.ViewHolder>(
    MOVIE_COMPARATOR
) {
    private var clickListener: IOnItemClick? = null

    fun setClickListener(listener: IOnItemClick?) {
        clickListener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.parentItem.animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.recycler_anim)
    }

    interface IOnItemClick {
        fun onItemClick(movie: Movies.Short)
    }

    internal inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var parentItem: LinearLayout = itemView.findViewById(R.id.parentItem)
        private var image: ImageView = itemView.findViewById(R.id.image)
        private var itemTitle: TextView = itemView.findViewById(R.id.itemTitle)

        fun bind(movie: Movies.Short) {
            itemView.setOnClickListener {
                clickListener?.onItemClick(movie)
            }
            image.requestLayout()
            itemTitle.text = movie.title

            image.load("${BuildConfig.API_IMAGE_BASE_URL}${movie.poster}") {
                transformations(RoundedCornersTransformation(16f))
            }
        }
    }

    companion object {
        val MOVIE_COMPARATOR = object : DiffUtil.ItemCallback<Movies.Short>() {
            override fun areContentsTheSame(oldItem: Movies.Short, newItem: Movies.Short): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: Movies.Short, newItem: Movies.Short): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}