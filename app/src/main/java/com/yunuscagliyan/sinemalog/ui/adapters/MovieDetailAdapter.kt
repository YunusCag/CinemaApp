package com.yunuscagliyan.sinemalog.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.yunuscagliyan.sinemalog.R
import com.yunuscagliyan.sinemalog.data.api.POSTER_BASE_URL
import com.yunuscagliyan.sinemalog.data.models.Movie
import com.yunuscagliyan.sinemalog.databinding.ItemMovieDetailBinding
import java.text.DateFormat.getDateInstance
import java.text.SimpleDateFormat

class MovieDetailAdapter : PagingDataAdapter<Movie, MovieDetailAdapter.MovieViewHolder>(MOVIE_COMPARATOR) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieDetailAdapter.MovieViewHolder {
        val binding =
            ItemMovieDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieDetailAdapter.MovieViewHolder, position: Int) {
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

    class MovieViewHolder(private val binding: ItemMovieDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindMovie(movie: Movie) {
            val moviePosterURL = POSTER_BASE_URL + movie!!.posterPath
            binding.ivMoviePoster.transitionName=moviePosterURL
            binding.tvMovieTitle.transitionName="${movie.id}"
            binding.apply {
                Glide.with(itemView)
                    .load(moviePosterURL)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(ivMoviePoster)
                tvMovieTitle.text = "${movie.title}"
                tvMovieOverview.text="${movie.overview}"
                tvVoteAverage.text = "${movie.voteAverage}/10"
                val convertFormatter=SimpleDateFormat("yyyy-MM-dd")
                var releaseDate=convertFormatter.parse(movie.releaseDate)
                val date=getDateInstance().format(releaseDate)
                tvYear.text=date
            }
            itemView.setOnClickListener {view->
                val extras= FragmentNavigatorExtras(
                    binding.ivMoviePoster to moviePosterURL,
                    binding.tvMovieTitle to "${movie.id}"
                )
                val bundle= bundleOf("movieId" to movie.id,"moviePosterURL" to moviePosterURL)
                view.findNavController().navigate(R.id.action_movie_detail,bundle,null,extras)

            }
        }
    }
}