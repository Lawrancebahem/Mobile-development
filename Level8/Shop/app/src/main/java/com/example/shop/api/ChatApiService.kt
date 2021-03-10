package com.example.shop.api

import com.example.shop.model.Conversation
import com.example.shop.model.Message
import retrofit2.http.*

interface ChatApiService {


    @GET("conversation/get-user-conversations/{id}")
    suspend fun getUserConversation(@Path("id") id:Long):ArrayList<Conversation>

    @POST("conversation/add-message-to-conversation/{conversationId}")
    suspend fun addMessageToConversation(@Path("conversationId") conversationId:Long, @Body message: Message):Boolean


    @POST("conversation/new-conversation")
    suspend fun addNewConversation(@Body conversation: Conversation):Boolean


    @POST("conversation/get-messages-conversation/{conversationId}")
    suspend fun getConversationsMessages(@Path("conversationId")conversationId:Long):ArrayList<Message>
}