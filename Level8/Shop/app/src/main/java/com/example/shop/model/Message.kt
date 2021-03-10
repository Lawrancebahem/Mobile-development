package com.example.shop.model

class Message(

    val id: Long? = null,
    val text: String? = null,
    val date: String = "",
    val sender: Long? = null,
    val receiver: Long? = null,
    val read: Boolean? = null

){

    override fun toString(): String {
        return "Message(id=$id, text=$text, date='$date', sender=$sender, receiver=$receiver, read=$read)"
    }
}