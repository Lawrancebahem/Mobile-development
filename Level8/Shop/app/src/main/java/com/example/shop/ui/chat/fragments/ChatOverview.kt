package com.example.shop.ui.chat.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shop.R
import com.example.shop.databinding.FragmentChatOverviewBinding
import com.example.shop.model.Conversation
import com.example.shop.model.ConversationComparator
import com.example.shop.model.User
import com.example.shop.ui.chat.adapter.ChatOverviewAdapter
import com.example.shop.ui.chat.viewmodel.ChatViewModel
import com.example.shop.ui.main.viewModel.UserDatabaseViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class ChatOverview : Fragment() {

    private var _binding: FragmentChatOverviewBinding? = null

    private val chatViewModel: ChatViewModel by activityViewModels()
    private lateinit var userDatabaseViewModel: UserDatabaseViewModel

    private lateinit var currentUser:LiveData<User>

    private lateinit var chatOverviewAdapter:ChatOverviewAdapter
    private lateinit var conversationList:ArrayList<Conversation>

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentChatOverviewBinding.inflate(inflater, container, false)
        return binding.root

    }

    @ExperimentalStdlibApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

        val actionBar: ActionBar? = (activity as AppCompatActivity?)!!.supportActionBar
        actionBar?.title = getString(R.string.chat).uppercase()
    }

    //setup
    private fun init(){
        userDatabaseViewModel = ViewModelProvider(this).get(UserDatabaseViewModel::class.java)

        //get the current user
        currentUser = userDatabaseViewModel.userRepository.getUser()

        //get conversations
        getUserConversations()

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rcChat.layoutManager = layoutManager

        //observe the retrieved conversations
        chatViewModel.userConversations.observe(viewLifecycleOwner){
            conversationList = it
            conversationList.sortWith(ConversationComparator())
            currentUser.observe(viewLifecycleOwner){user->
                chatOverviewAdapter = ChatOverviewAdapter(conversationList, user.id, ::onPreviewConversation)
                binding.rcChat.adapter = chatOverviewAdapter
            }
        }
    }


    /**
     * To get all conversations of the current user
     */
    private fun getUserConversations(){
        //get the user id and retrieve his conversations
        currentUser.observe(viewLifecycleOwner){
            chatViewModel.getUserConversations(it.id, currentUser.value!!.verificationToken!!.token!!)
        }
    }

    /**
     * Navigate to chat room, and set messages that are received as read
     */
    private fun onPreviewConversation(position:Int){
        chatViewModel.selectedConversation.value = conversationList[position]
        chatViewModel.currentUserId.value = currentUser.value!!.id

        var senderId = if (conversationList[position].user1!!.id ==  currentUser.value!!.id){
            conversationList[position].user2!!.id
        }else{
            conversationList[position].user1!!.id

        }
        val token = currentUser.value!!.verificationToken!!.token
        chatViewModel.setMessagesRead(conversationList[position].id!!,  senderId, token!!)
        findNavController().navigate(R.id.action_chatOverview_to_chatRoom)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}