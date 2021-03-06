package com.example.shop.api

import com.example.shop.model.Conversation
import com.example.shop.model.Message
import retrofit2.http.*

interface ChatApiService {


    @GET("conversation/get-user-conversations/{id}")
    suspend fun getUserConversation(@Path("id") id:Long, @Header("Authorization") auth:String):ArrayList<Conversation>

    @POST("conversation/add-message-to-conversation/{conversationId}")
    suspend fun addMessageToConversation(@Path("conversationId") conversationId:Long, @Body message: Message, @Header("Authorization") auth:String):Boolean


    @POST("conversation/new-conversation")
    suspend fun addNewConversation(@Body conversation: Conversation, @Header("Authorization") auth:String):Boolean


    @GET("conversation/get-messages-conversation/{conversationId}")
    suspend fun getConversationsMessages(@Path("conversationId")conversationId:Long, @Header("Authorization") auth:String):ArrayList<Message>

    @POST("conversation/set-read/{conversationId}/{userId}")
    suspend fun setMessagesRead(@Path("conversationId") conversationId: Long, @Path("userId") userId:Long, @Header("Authorization") auth:String)
}