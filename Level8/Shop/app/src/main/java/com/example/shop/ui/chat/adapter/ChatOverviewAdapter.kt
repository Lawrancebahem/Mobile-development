package com.example.shop.ui.chat.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shop.R
import com.example.shop.databinding.ItemChatBinding
import com.example.shop.model.Conversation
import com.example.shop.model.User
import com.example.shop.utility.ImageConverter

class ChatOverviewAdapter(private val conversationsList: ArrayList<Conversation>,
                          val currentUserId:Long,
                          val onPreviewClick:(Int) -> Unit):
                            RecyclerView.Adapter<ChatOverviewAdapter.ChatOverviewViewHolder>(){


    inner class ChatOverviewViewHolder(view: View):RecyclerView.ViewHolder(view), View.OnClickListener{

        init {
            view.setOnClickListener(this)
        }

        private val binding = ItemChatBinding.bind(view)

        fun bindData(conversation: Conversation){
            val secondUser:User

            //get the user that this current user is communicating with
            secondUser = if (conversation.user1!!.id != currentUserId){
                conversation.user1
            }else{
                conversation.user2!!
            }
            //set the profile picture of the second user
            if (secondUser.profilePicture != "") {
                val userPic = secondUser.profilePicture
                binding.usrPic.setImageBitmap(ImageConverter.decode(userPic!!))
            } else {
                binding.usrPic.setImageResource(R.drawable.profile_picture)
            }

            binding.usrNm.text = secondUser.firstName + " " +secondUser.lastName
            //get last message
            binding.msgUsr.text = conversation.messages!![conversation.messages.size-1].text
            binding.msgDt.text = conversation.messages[conversation.messages.size-1].date

        }

        //when previewing a chat
        override fun onClick(v: View?) {
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION){
                onPreviewClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatOverviewViewHolder {
        return ChatOverviewViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false))
    }

    override fun onBindViewHolder(holder: ChatOverviewViewHolder, position: Int) {
        holder.bindData(conversationsList[position])
    }

    override fun getItemCount(): Int {
        return conversationsList.size
    }
}