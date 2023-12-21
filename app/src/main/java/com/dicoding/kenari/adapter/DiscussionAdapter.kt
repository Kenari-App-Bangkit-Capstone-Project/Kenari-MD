package com.dicoding.kenari.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.kenari.R
import com.dicoding.kenari.api.Discussion
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class DiscussionAdapter(private val discussionList: List<Discussion>?) : RecyclerView.Adapter<DiscussionAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDetailName: TextView = itemView.findViewById(R.id.tv_detail_name)
        val tvStatus: TextView = itemView.findViewById(R.id.tv_status)
        val textDescription: TextView = itemView.findViewById(R.id.textDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.forum_item, parent, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val discussion = discussionList?.get(position)

        // Set data to views
        if (discussion?.isAnonymous.toBoolean()) {
            holder.tvDetailName.text = "Anonim"
        } else {
            holder.tvDetailName.text = discussion?.user?.name
        }
        holder.tvStatus.text = dateFormatted(discussion?.createdAt)
        holder.textDescription.text = discussion?.content
    }

    override fun getItemCount(): Int {
        return discussionList?.size ?: 0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun dateFormatted(date: String?): String {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
        val instant = Instant.from(inputFormatter.parse(date))
        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy  •  HH:mm ")
        val normalizedDateString = outputFormatter.format(localDateTime)

        return normalizedDateString.toString()
    }
}