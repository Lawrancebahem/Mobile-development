package com.example.madlevel2task1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.madlevel2task1.databinding.ItemPlaceBinding

class PlaceAdapter (private val places:List<Place>): RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {


    inner class ViewHolder (item:View):RecyclerView.ViewHolder(item){
        private val binding = ItemPlaceBinding.bind(item)

        fun dataBind(place:Place){
            binding.tvPlace.text = place.name
            binding.ivPlace.setImageResource(place.imageResId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_place,parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dataBind(places[position])
    }

    override fun getItemCount(): Int {
        return places.size
    }

}

