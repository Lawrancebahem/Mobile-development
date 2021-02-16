package com.example.madlevel6task1.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.madlevel6task1.R
import com.example.madlevel6task1.databinding.ItemColorBinding
import com.example.madlevel6task1.model.ColorItem

class ColorAdapter(private val colorList:ArrayList<ColorItem>, private val onClick: (ColorItem) -> Unit):RecyclerView.Adapter<ColorAdapter.ColorViewHolder>(){

    private lateinit var context: Context



    inner class ColorViewHolder(view:View):RecyclerView.ViewHolder(view){
        init {
            itemView.setOnClickListener { onClick(colorList[adapterPosition]) }
        }

        private val binding = ItemColorBinding.bind(view)
        fun bindData(colorItem: ColorItem){
            Glide.with(context).load(colorItem.getImageUrl()).into(binding.ivColor)

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        context = parent.context
        return ColorViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_color,parent, false))
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        holder.bindData(colorList[position])
    }

    override fun getItemCount(): Int {
        return colorList.size
    }
}