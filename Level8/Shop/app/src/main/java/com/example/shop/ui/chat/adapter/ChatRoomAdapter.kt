package com.example.shop.ui.chat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shop.R
import com.example.shop.model.Message
import java.text.SimpleDateFormat
import java.util.*

class ChatRoomAdapter(val messages: ArrayList<Message>, val currentUserId: Long) :
    RecyclerView.Adapter<ChatRoomAdapter.ChatRoomViewHolder>() {

    inner class ChatRoomViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        private val textField: TextView = view.findViewById(R.id.msgText)
        private val msTextField: TextView = view.findViewById(R.id.mgDate)
        private var date: Date? = null
        private val sdf = SimpleDateFormat("MMM-dd-yyyy HH:mm:ss aaa")
        private val justTime = SimpleDateFormat("hh:mm aaa")
        fun bindData(message: Message) {
            date = sdf.parse(message.date)
            textField.text = message.text
            msTextField.text = justTime.format(date!!)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (messages[position].sender == currentUserId) {
            return R.layout.item_my_message
        }
        return R.layout.item_user_message
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder {
        return ChatRoomViewHolder(
            LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {
        holder.bindData(messages[position])
    }

    override fun getItemCount(): Int {
        return messages.size
    }
}