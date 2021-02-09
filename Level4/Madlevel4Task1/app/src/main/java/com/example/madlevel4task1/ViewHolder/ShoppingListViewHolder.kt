package com.example.madlevel4task1.ViewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.madlevel4task1.Model.Product
import com.example.madlevel4task1.databinding.ItemPorductBinding

class ShoppingListViewHolder(view: View): RecyclerView.ViewHolder(view){

    private val binding = ItemPorductBinding.bind(view)

    fun bindData(product: Product){
        binding.rvName.text = product.name
        binding.rvAmount.text = product.amount.toString()
    }
}