package com.example.shop.ui.chat.adapter

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shop.R
import com.example.shop.model.Message
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class ChatRoomAdapter(val messages: ArrayList<Message>, val currentUserId: Long) :
    RecyclerView.Adapter<ChatRoomAdapter.ChatRoomViewHolder>() {
    private val sdf = SimpleDateFormat("MMM-dd-yyyy HH:mm:ss aaa")
    private val justTime = SimpleDateFormat("hh:mm aaa")
    private val TODAY = "Today"
    private val YESTERDAY = "Yesterday"

    inner class ChatRoomViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        private val textField: TextView = view.findViewById(R.id.msgText)
        private val msTextField: TextView = view.findViewById(R.id.mgDate)
        private val dateTextView: TextView = view.findViewById(R.id.topDateTextView)
        private var date: Date? = null


        fun bindData(message: Message) {
            date = sdf.parse(message.date)
            textField.text = message.text
            msTextField.text = justTime.format(date!!)

            if (adapterPosition != 0) {
                processDate(dateTextView, message.date, messages[adapterPosition - 1].date, false)
            } else {
                processDate(dateTextView, message.date, null, true)
            }
            if (currentUserId == message.sender) {
                val button: Button = view.findViewById(R.id.chk)
                if (message.read!!) {
                    button.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.twochecks, 0, 0)
                } else {
                    button.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.onecheck,0 , 0)
                }
            }
        }

        /**
         * Return the date above the message either it's today, yesterday or a date
         */
        private fun processDate(
            tv: TextView,
            dateAPIStr: String,
            dateAPICompareStr: String?,
            isFirstItem: Boolean
        ) {
            val f = SimpleDateFormat("MMM-dd-yyyy")
            if (isFirstItem) {
                //first item always got date/today to shows
                //and overkill to compare with next item flow
                var dateFromAPI: Date? = null
                try {
                    dateFromAPI = f.parse(dateAPIStr)
                    if (DateUtils.isToday(dateFromAPI.time)) tv.text = TODAY
                    else if (DateUtils.isToday(dateFromAPI.time + DateUtils.DAY_IN_MILLIS)) tv.text =
                        YESTERDAY
                    else tv.text = dateFromAPI.toString()
                    tv.includeFontPadding = false
                    tv.visibility = View.VISIBLE
                } catch (e: ParseException) {
                    e.printStackTrace()
                    tv.visibility = View.GONE
                }
            } else {
                val dateOne = f.parse(dateAPIStr)
                val dateTwo = f.parse(dateAPICompareStr!!)
                if (!dateOne!!.toString().equals(dateTwo!!.toString(), ignoreCase = true)) {
                    try {
                        if (DateUtils.isToday(dateOne.time)) tv.text = TODAY
                        else if (DateUtils.isToday(dateOne.time + DateUtils.DAY_IN_MILLIS)) tv.text =
                            YESTERDAY
                        else tv.text = dateOne.toString()
                        tv.includeFontPadding = false
                        tv.visibility = View.VISIBLE
                    } catch (e: ParseException) {
                        e.printStackTrace()
                        tv.visibility = View.GONE
                    }
                } else {
                    tv.visibility = View.GONE
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (messages[position].sender == currentUserId) {
            return R.layout.item_my_message
        }
        return R.layout.item_user_message
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder {
        return ChatRoomViewHolder(
            LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {
        holder.bindData(messages[position])
    }

    override fun getItemCount(): Int {
        return messages.size
    }
}