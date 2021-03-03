package com.example.shop.adapter

import android.content.Context
import android.widget.ArrayAdapter
import com.example.shop.model.Category


internal class SpinnerAdapter(context: Context, resource: Int, objects: List<Category>) : ArrayAdapter<Category?>(context, resource, objects){
    override fun getCount(): Int {
        return super.getCount() - 1 // This makes not show the last item
    }

    override fun getItem(position: Int): Category? {
        return super.getItem(position)
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }
}