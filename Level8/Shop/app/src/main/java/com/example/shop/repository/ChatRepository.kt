package com.example.shop.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shop.api.Api
import com.example.shop.api.ApiError
import com.example.shop.api.ChatApiService
import com.example.shop.model.Conversation
import com.example.shop.model.Message
import retrofit2.http.POST
import retrofit2.http.Query

class ChatRepository {

    private val chatApiService: ChatApiService = Api(ChatApiService::class.java).createService()

    private val _userConversations:MutableLiveData<ArrayList<Conversation>> = MutableLiveData()
    val userConversations:LiveData<ArrayList<Conversation>> get() =  _userConversations

    private val _success:MutableLiveData<Boolean> = MutableLiveData()

    private val _conversationMessages:MutableLiveData<ArrayList<Message>> = MutableLiveData()

    val  conversationMessages:LiveData<ArrayList<Message>> get() = _conversationMessages

    private val _refreshedMessage:MutableLiveData<ArrayList<Message>> = MutableLiveData()

    val refreshedMessage:LiveData<ArrayList<Message>> = _refreshedMessage
    /**
     * get all conversations of user
     */
    suspend fun getUserConversations(id:Long, auth:String){
        try {
            _userConversations.value = chatApiService.getUserConversation(id, auth)

        } catch (error: Throwable) {
            val message = ApiError.getErrorMessage(error.message.toString())
            throw ApiError(message, error)
        }
    }

    /**
     * add message to conversation
     */
    suspend fun addMessageToConversation(conversationId:Long, message: Message, auth:String){

        try {
           _success.value = chatApiService.addMessageToConversation(conversationId, message, auth)

        } catch (error: Throwable) {
            val message = ApiError.getErrorMessage(error.message.toString())
            throw ApiError(message, error)
        }
    }

    /**
     * add new conversation between two users
     */
    suspend fun addNewConversation( conversation: Conversation, auth:String){
        try {
            _success.value = chatApiService.addNewConversation(conversation, auth)

        } catch (error: Throwable) {
            val message = ApiError.getErrorMessage(error.message.toString())
            throw ApiError(message, error)
        }

    }

    /**
     * get all messages of certain conversation
     */
    suspend fun getConversationsMessages(conversationId:Long, auth:String){
        try {
            _conversationMessages.value = chatApiService.getConversationsMessages(conversationId, auth)
        } catch (error: Throwable) {
            val message = ApiError.getErrorMessage(error.message.toString())
            throw ApiError(message, error)
        }
    }

    /**
     * To set messages as read
     */
    suspend fun setMessagesRead(conversationId: Long,userId:Long, auth:String){
        try {
         chatApiService.setMessagesRead(conversationId, userId, auth)
        } catch (error: Throwable) {
            val message = ApiError.getErrorMessage(error.message.toString())
            throw ApiError(message, error)
        }
    }
}