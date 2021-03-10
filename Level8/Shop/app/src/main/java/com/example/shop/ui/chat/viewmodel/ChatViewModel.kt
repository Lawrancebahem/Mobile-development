package com.example.shop.ui.chat.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shop.model.Conversation
import com.example.shop.model.Message
import com.example.shop.model.User
import com.example.shop.repository.ChatRepository
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    private val chatRepository: ChatRepository = ChatRepository()


    val userConversations: LiveData<ArrayList<Conversation>> = chatRepository.userConversations

    val conversationMessages: LiveData<ArrayList<Message>> = chatRepository.conversationMessages

    val error: MutableLiveData<String> = MutableLiveData()

    val selectedConversations:MutableLiveData<Conversation> = MutableLiveData()

    val currentUserId:MutableLiveData<Long> = MutableLiveData()
    /**
     * get all conversations of user
     */
    fun getUserConversations(id: Long) {
        viewModelScope.launch {
            try {
                chatRepository.getUserConversations(id)

            } catch (err: Throwable) {
                error.value = err.message
            }
        }
    }


    /**
     * add message to conversation
     */
    fun addMessageToConversation(conversationId: Long, message: Message) {

        viewModelScope.launch {
            try {
                chatRepository.addMessageToConversation(conversationId, message)

            } catch (err: Throwable) {
                error.value = err.message
            }
        }

    }

    /**
     * add new conversation between two users
     */
    fun addNewConversation(conversation: Conversation) {
        viewModelScope.launch {
            try {
                chatRepository.addNewConversation(conversation)

            } catch (err: Throwable) {
                error.value = err.message
            }
        }
    }

    /**
     * get all messages of certain conversation
     */
    fun getConversationsMessages(conversationId: Long) {
        viewModelScope.launch {
            try {
                chatRepository.getConversationsMessages(conversationId)

            } catch (err: Throwable) {
                error.value = err.message
            }
        }
    }
}