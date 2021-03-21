package com.example.shop.ui.chat.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shop.databinding.FragmentChatRoomBinding
import com.example.shop.model.Message
import com.example.shop.model.User
import com.example.shop.ui.chat.adapter.ChatRoomAdapter
import com.example.shop.ui.chat.viewmodel.ChatViewModel
import com.example.shop.ui.main.viewModel.UserDatabaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */

@AndroidEntryPoint
class ChatRoom : Fragment() {

    private var _binding: FragmentChatRoomBinding? = null
    private val chatViewModel: ChatViewModel by activityViewModels()

    private lateinit var messagesList: ArrayList<Message>
    private lateinit var chatRoomAdapter: ChatRoomAdapter

    private var receiverId: Long? = null

    private lateinit var currentUser: LiveData<User>
    private lateinit var userDatabaseViewModel: UserDatabaseViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatRoomBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

        binding.btnSend.setOnClickListener {
            sendMessage()
        }
    }


    /**
     * Setup for the adpater
     */
    private fun init() {

        userDatabaseViewModel = ViewModelProvider(this).get(UserDatabaseViewModel::class.java)

        //get the current user
        currentUser = userDatabaseViewModel.userRepository.getUser()

        chatViewModel.selectedConversation.observe(viewLifecycleOwner) { conversation ->
            messagesList = conversation.messages!!
            currentUser.observe(viewLifecycleOwner) { currentUser ->
                //get receiver id
                receiverId = if (conversation.user1!!.id != currentUser.id) {
                    conversation.user1.id
                } else {
                    conversation.user2!!.id
                }

                chatRoomAdapter = ChatRoomAdapter(messagesList, currentUser.id)
                val layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                layoutManager.stackFromEnd = true
                binding.rcRoom.layoutManager = layoutManager
                binding.rcRoom.adapter = chatRoomAdapter
                //refreshMessages
                getConversationMessages()
            }
        }
    }


    /**
     * To handle the click button, when sending a message
     */
    private fun sendMessage() {
        val txtMessage = binding.txt.text.toString()

        if (txtMessage.isNotEmpty()) {
            val message = Message(
                null,
                txtMessage,
                chatViewModel.sdf.format(Date()),
                chatViewModel.currentUserId.value,
                receiverId,
                false
            )
            binding.txt.text.clear()
            messagesList.add(message)
            chatRoomAdapter.notifyDataSetChanged()
            binding.rcRoom.smoothScrollToPosition(messagesList.size - 1)

            chatViewModel.addMessageToConversation(
                chatViewModel.selectedConversation.value!!.id!!,
                message,
                currentUser.value!!.verificationToken!!.token!!
            )

        }
    }

    /**
     * To refresh the conversation, it checks first if there are new messages and after that updates the conversation
     */
    private fun getConversationMessages() {
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object : Runnable {
            override fun run() {
                chatViewModel.getConversationsMessages(chatViewModel.selectedConversation.value!!.id!!,currentUser.value!!.verificationToken!!.token!!)
                if (view != null) {
                    chatViewModel.conversationMessages.observe(viewLifecycleOwner) { messages ->
                        if (messages.size != messagesList.size) {
                            messagesList.clear()
                            messagesList.addAll(messages)
                            chatRoomAdapter.notifyDataSetChanged()
                            binding.rcRoom.scrollToPosition(messagesList.size - 1)
                        }
                    }
                    mainHandler.postDelayed(this, 5000)
                }
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}