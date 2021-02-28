package com.example.shop.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shop.R
import com.example.shop.databinding.ItemImageBinding

class ImageAdapter(private val bitmapList:ArrayList<Bitmap>,  private val listener:(Int) -> Unit):RecyclerView.Adapter<ImageAdapter.ImageViewHolder>(){


    inner class ImageViewHolder(view:View): RecyclerView.ViewHolder(view),View.OnClickListener{



        private val binding = ItemImageBinding.bind(view)
        fun bind(bitmap:Bitmap){
            binding.imageView3.setImageBitmap(bitmap)
            binding.imageView3.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val index = adapterPosition
            if (index != RecyclerView.NO_POSITION){
                listener(index)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_image,parent, false))
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(bitmapList[position])
    }

    override fun getItemCount(): Int {
        return bitmapList.size
    }
}