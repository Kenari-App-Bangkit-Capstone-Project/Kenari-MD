package com.dicoding.kenari.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.kenari.R
import com.dicoding.kenari.api.ChatbotHistory
import com.dicoding.kenari.view.chatbot.ChatbotChat

class ChatbotAdapter (private val chatMessages: List<ChatbotChat>) : RecyclerView.Adapter<ChatbotAdapter.ChatViewHolder>()  {
    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userTextView: TextView = itemView.findViewById(R.id.user_message)
        val botTextView: TextView = itemView.findViewById(R.id.bot_message)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chatMessage = chatMessages[position]
        holder.userTextView.text = chatMessage.user_input
        holder.botTextView.text = chatMessage.response
    }

    override fun getItemCount(): Int {
        return chatMessages.size
    }
}