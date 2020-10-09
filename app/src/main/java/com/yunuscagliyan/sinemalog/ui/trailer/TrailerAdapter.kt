package com.yunuscagliyan.sinemalog.ui.trailer

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener
import com.yunuscagliyan.sinemalog.data.models.VideoResponse
import com.yunuscagliyan.sinemalog.databinding.ItemTrailerBinding

class TrailerAdapter(

):RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder>() {
    private var trailerList:List<VideoResponse.Video?> = mutableListOf()

    fun submitList(list:List<VideoResponse.Video?>){
        this.trailerList=list
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailerViewHolder {
        val binding=ItemTrailerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TrailerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrailerViewHolder, position: Int) {
        val trailer=this.trailerList[position]
        holder.bindPlayer(trailer!!)
    }

    override fun getItemCount(): Int {
        return this.trailerList.size
    }

    class TrailerViewHolder(val binding:ItemTrailerBinding):RecyclerView.ViewHolder(binding.root){
        fun bindPlayer(video:VideoResponse.Video){
            binding.apply {
                val videoId=video.key
                youtubePlayerView.initialize({ youTubePlayer ->
                    youTubePlayer.addListener(object: AbstractYouTubePlayerListener(){
                        override fun onReady() {
                            youTubePlayer.loadVideo(videoId!!,0f)
                            youTubePlayer.pause()
                        }
                    })
                },true)
            }
        }
    }


}