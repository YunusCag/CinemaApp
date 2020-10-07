package com.yunuscagliyan.sinemalog.ui.movie_detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.yunuscagliyan.sinemalog.R
import com.yunuscagliyan.sinemalog.data.api.POSTER_BASE_URL
import com.yunuscagliyan.sinemalog.data.models.Cast
import com.yunuscagliyan.sinemalog.databinding.ItemCastBinding

class CastAdapter():RecyclerView.Adapter<CastAdapter.CastViewHolder>(){

    private var castList:List<Cast?> = mutableListOf()



    fun submitList(list:List<Cast?>){
        this.castList=list
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        val binding=ItemCastBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        val cast=this.castList[position]
        holder.bind(cast!!)
    }

    override fun getItemCount(): Int {
        return this.castList.size
    }
    class CastViewHolder(val binding:ItemCastBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(cast:Cast){
            val profileURL = POSTER_BASE_URL + cast!!.profilePath
            binding.apply {
                Glide.with(itemView)
                    .load(profileURL)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(ivCastProfile)
                tvCharacterName.text=cast.character
                tvPlayerName.text=cast.name
            }
        }
    }
}