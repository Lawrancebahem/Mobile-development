package com.example.shop.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shop.R
import com.example.shop.databinding.ItemProductBinding
import com.example.shop.model.Product
import com.example.shop.utility.ImageConverter

class ProductAdapter(val productList: List<Product>, val onPreview: (Int) -> Unit, val addLike: (Long) -> Unit, val removeLike: (Long) -> Unit, val usersLikes: Set<Product>)
    : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        init {
            view.setOnClickListener(this)
        }

        val binding = ItemProductBinding.bind(view)
        fun bindData(product: Product) {
            val sampleImageBitmap = ImageConverter.decode(product.images!![0])
            binding.imagePro.setImageBitmap(sampleImageBitmap)
            binding.titlePro.text = product.title
            binding.descPro.text = product.description
            binding.pricePr.text = "€" + product.price.toString()

//            for (i in usersLikes) {
//                if (i.title == product.title) {
//                    binding.likeBtn.text = "c"
//                    binding.likeBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.heart_filled, 0, 0)
//                }
//            }

//            //to check if the user has this product in his list
            if (usersLikes.contains(product)) {
                binding.likeBtn.text = "c"
                binding.likeBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.heart_filled, 0, 0)
            }else{
                binding.likeBtn.text = ""
                binding.likeBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.heart, 0, 0)
            }

            //when the user clicks on a like button either we add it to database or we remove it
            binding.likeBtn.setOnClickListener {
                if (binding.likeBtn.text == "") {
                    binding.likeBtn.text = "c"
                    addLike(product.id!!)
                    binding.likeBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.heart_filled, 0, 0)
                } else {
                    binding.likeBtn.text = ""
                    removeLike(product.id!!)
                    binding.likeBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.heart, 0, 0)
                }
            }

        }

        override fun onClick(v: View?) {
            val index = adapterPosition
            if (index != RecyclerView.NO_POSITION) {
                onPreview(index)
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