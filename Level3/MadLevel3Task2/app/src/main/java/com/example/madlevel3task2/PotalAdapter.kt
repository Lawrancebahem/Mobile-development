package com.example.madlevel3task2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.madlevel3task2.databinding.ItemPortalBinding
import kotlinx.android.synthetic.main.item_portal.view.*

class PortalAdapter(val portalsList: ArrayList<Portal>, var listener: OnItemClick) : RecyclerView.Adapter<PortalAdapter.PortalViewHolder>() {

    inner class PortalViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener, View.OnLongClickListener {

        //once it's initialized set listeners to this view (PortalViewHolder)
        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }


        private val binding = ItemPortalBinding.bind(view)

        fun dataBind(portal: Portal) {
            binding.itemViewName.text = portal.name
            binding.itemViewPortal.text = portal.url
        }


        //When the user clicks on an item, will be directed to the browser
        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }

        //On long Click to remove a certain portal
        override fun onLongClick(v: View?): Boolean {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                portalsList.removeAt(position)
                notifyDataSetChanged()
                return true
            }
            return false
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PortalViewHolder {
        return PortalViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_portal, parent, false))
    }

    override fun onBindViewHolder(holder: PortalViewHolder, position: Int) {
        holder.dataBind(portalsList[position])
    }

    override fun getItemCount(): Int {
        return portalsList.size
    }


}