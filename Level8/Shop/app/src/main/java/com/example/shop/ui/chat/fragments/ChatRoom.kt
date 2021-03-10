package com.example.shop.ui.chat.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shop.databinding.FragmentChatRoomBinding
import com.example.shop.model.Message
import com.example.shop.ui.chat.adapter.ChatRoomAdapter
import com.example.shop.ui.chat.viewmodel.ChatViewModel

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ChatRoom : Fragment() {

    private var _binding: FragmentChatRoomBinding? = null
    private val chatViewModel: ChatViewModel by activityViewModels()

    private lateinit var messagesList:ArrayList<Message>
    private lateinit var chatRoomAdapter:ChatRoomAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentChatRoomBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        chatViewModel.selectedConversations.observe(viewLifecycleOwner){conversation ->
            messagesList = conversation.messages!!
            chatViewModel.currentUserId.observe(viewLifecycleOwner){ currentUser->
                chatRoomAdapter = ChatRoomAdapter(messagesList, currentUser)
                val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                binding.rcRoom.layoutManager = layoutManager
                binding.rcRoom.adapter = chatRoomAdapter
            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}