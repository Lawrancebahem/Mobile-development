package com.example.shop.model

import java.text.SimpleDateFormat

class ConversationComparator:Comparator<Conversation> {

    private val sdf = SimpleDateFormat("MMM-dd-yyyy HH:mm:ss aaa")
    override fun compare(o1: Conversation?, o2: Conversation?): Int {
        val messageOneDate = sdf.parse(o1!!.messages!![o1.messages!!.size-1].date)
        val messageTowDate = sdf.parse(o2!!.messages!![o2.messages!!.size-1].date)
        return -messageOneDate!!.compareTo(messageTowDate)
    }
}