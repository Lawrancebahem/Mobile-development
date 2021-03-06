package com.example.shop.ui.chat.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shop.model.Conversation
import com.example.shop.model.Message
import com.example.shop.repository.ChatRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class ChatViewModel : ViewModel() {


    //date format for the message
    val sdf = SimpleDateFormat("MMM-dd-yyyy hh:mm:ss aaa")

    private val chatRepository: ChatRepository = ChatRepository()

    val userConversations: LiveData<ArrayList<Conversation>> = chatRepository.userConversations

    val conversationMessages: LiveData<ArrayList<Message>> = chatRepository.conversationMessages

    val error: MutableLiveData<String> = MutableLiveData()

    val selectedConversation:MutableLiveData<Conversation> = MutableLiveData()

    val currentUserId:MutableLiveData<Long> = MutableLiveData()

    val sentSuccessfully:MutableLiveData<Boolean> = MutableLiveData()

    val refreshedMessages:LiveData<ArrayList<Message>> = chatRepository.refreshedMessage

    /**
     * get all conversations of user
     */
    fun getUserConversations(id: Long, auth:String) {
        viewModelScope.launch {
            try {
                chatRepository.getUserConversations(id, auth)

            } catch (err: Throwable) {
                error.value = err.message
            }
        }
    }


    /**
     * add message to conversation
     */
    fun addMessageToConversation(conversationId: Long, message: Message, auth:String) {

        viewModelScope.launch {
            try {
                chatRepository.addMessageToConversation(conversationId, message, auth)
                sentSuccessfully.value = true
            } catch (err: Throwable) {
                sentSuccessfully.value = false
                error.value = err.message
            }
        }

    }

    /**
     * add new conversation between two users
     */
    fun addNewConversation(conversation: Conversation, auth:String) {
        viewModelScope.launch {
            try {
                chatRepository.addNewConversation(conversation, auth)

            } catch (err: Throwable) {
                error.value = err.message
            }
        }
    }

    /**
     * get all messages of certain conversation
     */
    fun getConversationsMessages(conversationId: Long, auth:String) {
        viewModelScope.launch {
            try {
                chatRepository.getConversationsMessages(conversationId, auth)
            } catch (err: Throwable) {
                error.value = err.message
            }
        }
    }

    /**
     * To set messages as read
     */
    fun setMessagesRead(conversationId: Long,userId:Long, auth:String) {
        viewModelScope.launch {
            try {
                chatRepository.setMessagesRead(conversationId, userId, auth)
            } catch (err: Throwable) {
                error.value = err.message
            }
        }
    }

}