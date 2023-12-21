package com.dicoding.kenari.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.kenari.R
import com.dicoding.kenari.api.Comment
import com.dicoding.kenari.api.Discussion
import com.dicoding.kenari.view.discussion.DetailDiscussionActivity
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class DiscussionAdapter(private val context: Context, private val discussionList: List<Discussion>?) : RecyclerView.Adapter<DiscussionAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDetailName: TextView = itemView.findViewById(R.id.tv_detail_name)
        val tvStatus: TextView = itemView.findViewById(R.id.tv_status)
        val textDescription: TextView = itemView.findViewById(R.id.textDescription)

        val discussionLayout: ConstraintLayout = itemView.findViewById(R.id.layout_discussion_item)
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

        holder.discussionLayout.setOnClickListener {
            val intent = Intent(context, DetailDiscussionActivity::class.java)
            intent.putExtra("discussion_id", discussion?.id.toString())
            context.startActivity(intent)
        }
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

// Adapter untuk RecyclerView komentar
class CommentAdapter(private val comments: List<Comment>?) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.user_name)
        val commentDate: TextView = itemView.findViewById(R.id.comment_date)
        val commentText: TextView = itemView.findViewById(R.id.comment_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = comments?.get(position)

        holder.userName.text = comment?.user?.name
        holder.commentDate.text = dateFormatted(comment?.createdAt)
        holder.commentText.text = comment?.comment
    }

    override fun getItemCount(): Int {
        return comments?.size ?: 0
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
