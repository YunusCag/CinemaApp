package com.yunuscagliyan.sinemalog.ui.credit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.gms.ads.AdRequest
import com.yunuscagliyan.sinemalog.R
import com.yunuscagliyan.sinemalog.data.api.POSTER_BASE_URL
import com.yunuscagliyan.sinemalog.data.models.CreditResponse
import com.yunuscagliyan.sinemalog.databinding.ItemCreditMovieBinding

class CreditAdapter : RecyclerView.Adapter<CreditAdapter.CreditMovieViewHolder>() {

    private var creditList: List<CreditResponse.Person.KnownFor?> = mutableListOf()

    fun submitList(list: List<CreditResponse.Person.KnownFor?>) {
        this.creditList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditMovieViewHolder {
        val binding =
            ItemCreditMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CreditMovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CreditMovieViewHolder, position: Int) {
        val movie = this.creditList[position]
        holder.bindMovie(movie!!)
    }

    override fun getItemCount(): Int {
        return this.creditList.size
    }

    inner class CreditMovieViewHolder(val binding: ItemCreditMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindMovie(movie: CreditResponse.Person.KnownFor) {
            if (movie != null) {
                val moviePosterURL = POSTER_BASE_URL + movie!!.posterPath
                binding.ivMoviePoster.transitionName = moviePosterURL
                binding.tvMovieTitle.transitionName = "${movie.id}"
                binding.apply {
                    Glide.with(itemView)
                        .load(moviePosterURL)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.ic_error)
                        .into(ivMoviePoster)
                    if(movie.title!=null){
                        tvMovieTitle.text = "${movie.title}"
                    }else if(movie.originalName!=null){
                        tvMovieTitle.text = "${movie.originalName}"
                    }else{
                        tvMovieTitle.text=""
                    }
                    tvMediaType.text="${movie.mediaType}"
                    tvMovieOverview.text = "${movie.overview}"
                    tvVoteAverage.text = "${movie.voteAverage}/10"
                }
                initializeAd()
                if (movie.mediaType == "movie") {
                    itemView.setOnClickListener { view ->
                        val extras = FragmentNavigatorExtras(
                            binding.ivMoviePoster to moviePosterURL,
                            binding.tvMovieTitle to "${movie.id}"
                        )
                        val bundle =
                            bundleOf("movieId" to movie.id, "moviePosterURL" to moviePosterURL)
                        view.findNavController()
                            .navigate(R.id.action_movie_detail, bundle, null, extras)

                    }
                }

            }

        }
        private fun initializeAd() {
            binding.apply {
                if(isAdShow()){
                    layoutAd.visibility= View.VISIBLE
                    val adRequest= AdRequest.Builder()
                        .build()
                    adRequest.isTestDevice(binding.root.context)
                    adView.loadAd(adRequest)
                }else{
                    layoutAd.visibility= View.GONE
                }
            }
        }

        private fun isAdShow():Boolean{
            return absoluteAdapterPosition==itemCount-1
        }
    }


}