package com.yunuscagliyan.sinemalog.ui.category

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.yunuscagliyan.sinemalog.R
import com.yunuscagliyan.sinemalog.data.models.Genre
import com.yunuscagliyan.sinemalog.databinding.ItemCategoryBinding
import com.yunuscagliyan.sinemalog.utils.AppConstant
import kotlin.random.Random

class CategoryAdapter:RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    private var genreList:List<Genre> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding=ItemCategoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return  CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val genre= genreList[position]
        holder.bindUI(genre)
    }

    override fun getItemCount(): Int {
        return this.genreList.size
    }
    fun submitList(list:List<Genre>){
        this.genreList=list
        notifyDataSetChanged()
    }
    class CategoryViewHolder(val binding:ItemCategoryBinding):RecyclerView.ViewHolder(binding.root){

        fun bindUI(genre:Genre){
            val randomNum= Random.nextInt(0,AppConstant.colorList.size-1)
            binding.apply {
                tvGenreName.text=genre.name
                root.setBackgroundColor(Color.parseColor(AppConstant.colorList[randomNum]))
            }
            itemView.setOnClickListener {view->
                val bundle= bundleOf("genre" to genre)
                view.findNavController().navigate(R.id.category_detail,bundle,null)

            }

        }
    }


}