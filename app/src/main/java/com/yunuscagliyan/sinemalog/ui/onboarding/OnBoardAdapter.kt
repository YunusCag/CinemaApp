package com.yunuscagliyan.sinemalog.ui.onboarding

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yunuscagliyan.sinemalog.databinding.ItemOnBoardBinding
import com.yunuscagliyan.sinemalog.utils.AppConstant
import kotlin.random.Random

class OnBoardAdapter(
    private val textList:List<String>
):RecyclerView.Adapter<OnBoardAdapter.OnBoardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardViewHolder {
        val binding=ItemOnBoardBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return OnBoardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OnBoardViewHolder, position: Int) {
        holder.bindUI(textList[position])
    }

    override fun getItemCount(): Int {
        return this.textList.size
    }


    class OnBoardViewHolder(val binding:ItemOnBoardBinding):RecyclerView.ViewHolder(binding.root){

        fun bindUI(text:String){
            binding.apply {
                val randomNum= Random.nextInt(0, AppConstant.colorList.size-1)
                tvDescription.text=text
                root.setBackgroundColor(Color.parseColor(AppConstant.colorList[randomNum]))
            }
        }
    }


}