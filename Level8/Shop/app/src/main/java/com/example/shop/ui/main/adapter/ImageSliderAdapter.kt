package com.example.shop.ui.main.adapter

import android.graphics.Bitmap
import com.smarteist.autoimageslider.SliderViewAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shop.R
import com.example.shop.databinding.ImageSliderBinding

class ImageSliderAdapter(var bitmapList:ArrayList<Bitmap>) : SliderViewAdapter<ImageSliderAdapter.SliderAdapterVH>() {

    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH {
        return SliderAdapterVH(LayoutInflater.from(parent.context).inflate(R.layout.image_slider, parent, false))
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterVH, position: Int) {
        viewHolder.bindData(bitmapList[position])
    }

    override fun getCount(): Int {
        //slider view count could be dynamic size
        return bitmapList.size
    }

    inner class SliderAdapterVH(itemView: View) : ViewHolder(itemView) {

        val binding = ImageSliderBinding.bind(itemView)

        fun bindData(bitmap:Bitmap){
            binding.ivAutoImageSlider.setImageBitmap(bitmap)
        }
    }
}