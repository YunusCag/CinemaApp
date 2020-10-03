package com.yunuscagliyan.sinemalog.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Transition
import coil.load
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.yunuscagliyan.sinemalog.R
import com.yunuscagliyan.sinemalog.data.api.POSTER_BASE_URL
import com.yunuscagliyan.sinemalog.data.models.Movie
import com.yunuscagliyan.sinemalog.databinding.ItemUpComingMovieBinding

class UpComingAdapter :
    PagingDataAdapter<Movie, UpComingAdapter.UpComingViewHolder>(MOVIE_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpComingViewHolder {
        val binding =
            ItemUpComingMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return UpComingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UpComingViewHolder, position: Int) {
        val movie = getItem(position) as Movie

        holder.bind(movie)
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

    class UpComingViewHolder(
        private val binding: ItemUpComingMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            val moviePosterURL = POSTER_BASE_URL + movie!!.posterPath
            binding.apply {
                ivPoster.transitionName = moviePosterURL
                tvMovieTitle.transitionName = "${movie.id}"
                binding.ivPoster.apply{
                    load(moviePosterURL){
                        error(R.drawable.ic_error)
                        crossfade(true)
                        crossfade(200)
                        scaleType=ImageView.ScaleType.FIT_XY
                    }
                }
                tvMovieTitle.text = movie.title
                tvVoteAverage.text = "${movie.voteAverage}/10"
                itemView.setOnClickListener { view ->
                    val extras = FragmentNavigatorExtras(
                        binding.ivPoster to moviePosterURL,
                        binding.tvMovieTitle to "${movie.id}"
                    )
                    val bundle = bundleOf("movieId" to movie.id, "moviePosterURL" to moviePosterURL)
                    view.findNavController()
                        .navigate(R.id.movieDetailFragment, bundle, null, extras)

                }
            }
        }
    }


}