package com.example.shop.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shop.R
import com.example.shop.databinding.ItemProductBinding
import com.example.shop.model.Product
import com.example.shop.utility.ImageConverter

class ProductAdapter(val productList:List<Product>, val listener:(Int) -> Unit):RecyclerView.Adapter<ProductAdapter.ProductViewHolder>(){

    inner class ProductViewHolder(view:View):RecyclerView.ViewHolder(view), View.OnClickListener{

        init {
            view.setOnClickListener(this)
        }
        val binding = ItemProductBinding.bind(view)
        fun bindData(product:Product){
            val sampleImageBitmap = ImageConverter.decode(product.images!![0])
            binding.imagePro.setImageBitmap(sampleImageBitmap)
            binding.titlePro.text = product.title
            binding.descPro.text = product.description
            binding.pricePr.text = "€"+product.price.toString()
        }

        override fun onClick(v: View?) {
            val index = adapterPosition
            if (index != RecyclerView.NO_POSITION){
                listener(index)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bindData(productList[position])
    }

    override fun getItemCount(): Int {
        return productList.size
    }
}