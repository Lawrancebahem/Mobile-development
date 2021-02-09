package com.example.madlevel4task1.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.madlevel4task1.Model.Product
import com.example.madlevel4task1.R
import com.example.madlevel4task1.ViewHolder.ShoppingListViewHolder

class ShoppingListAdapter(private var shoppingList:ArrayList<Product>):RecyclerView.Adapter<ShoppingListViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListViewHolder {
        return ShoppingListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_porduct,parent,false))
    }

    override fun onBindViewHolder(holder: ShoppingListViewHolder, position: Int) {
        holder.bindData(shoppingList[position])
    }

    override fun getItemCount(): Int {
        return shoppingList.size
    }
}