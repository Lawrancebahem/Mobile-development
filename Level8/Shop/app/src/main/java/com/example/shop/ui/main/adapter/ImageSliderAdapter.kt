package com.example.shop.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.shop.R
import com.example.shop.databinding.ImageSliderBinding
import com.smarteist.autoimageslider.SliderViewAdapter

class ImageSliderAdapter(var imagesURL: ArrayList<String>) : SliderViewAdapter<ImageSliderAdapter.SliderAdapterVH>() {

    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH {
        context = parent.context
        return SliderAdapterVH(
            LayoutInflater.from(context).inflate(R.layout.image_slider, parent, false)
        )
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterVH, position: Int) {
        viewHolder.bindData(imagesURL[position])
    }

    override fun getCount(): Int {
        //slider view count could be dynamic size
        return imagesURL.size
    }

    inner class SliderAdapterVH(itemView: View) : ViewHolder(itemView) {

        val binding = ImageSliderBinding.bind(itemView)

        fun bindData(imageURL: String) {
            Glide.with(context).load(imageURL).into(binding.ivAutoImageSlider)
        }
    }
}