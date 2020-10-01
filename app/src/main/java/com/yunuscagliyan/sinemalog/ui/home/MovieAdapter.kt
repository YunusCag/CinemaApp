package com.yunuscagliyan.sinemalog.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.yunuscagliyan.sinemalog.R
import com.yunuscagliyan.sinemalog.data.api.POSTER_BASE_URL
import com.yunuscagliyan.sinemalog.data.models.Movie
import com.yunuscagliyan.sinemalog.databinding.ItemMovieBinding
import com.yunuscagliyan.sinemalog.databinding.ItemUpComingMovieBinding

class MovieAdapter : PagingDataAdapter<Movie, MovieAdapter.MovieViewHolder>(MOVIE_COMPARATOR) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieAdapter.MovieViewHolder {
        val binding =
            ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieAdapter.MovieViewHolder, position: Int) {
        val movie = getItem(position) as Movie

        holder.bindMovie(movie)
    }

    companion object {
        private val MOVIE_COMPARATOR = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }

        }
    }

    class MovieViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindMovie(movie: Movie) {
            val moviePosterURL = POSTER_BASE_URL + movie!!.posterPath
            binding.apply {
                Glide.with(itemView)
                    .load(moviePosterURL)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(ivMoviePoster)
                tvMovieTitle.text = "${movie.title}"
                tvMovieRating.text = "${movie.voteAverage}/10"
            }
        }
    }
}