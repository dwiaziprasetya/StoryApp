package com.example.storyapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.data.remote.response.ListStoryItem
import com.example.storyapp.databinding.ItemStoryBinding
import java.text.SimpleDateFormat
import java.util.Locale

class StoryAdapter : ListAdapter<ListStoryItem, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemCalback : OnItemCallback

    class MyViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            binding.tvDate.text = formatDate(story.createdAt)
            binding.tvDescription.text = story.description
            binding.tvUsername.text = story.name.uppercase(Locale.getDefault())
            Glide.with(itemView)
                .load(story.photoUrl)
                .into(binding.imgImageStory)
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val story = getItem(position)
        holder.bind(story)
        holder.itemView.setOnClickListener {
            onItemCalback.onItemClicked(story)
        }
    }

    companion object {
        val DIFF_CALLBACK = object  : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    fun setOnItemClickCallback(onItemCallback: OnItemCallback) {
        this.onItemCalback = onItemCallback
    }

    interface OnItemCallback {
        fun onItemClicked(story: ListStoryItem)
    }
}

fun formatDate(dateString: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    val date = inputFormat.parse(dateString)
    return outputFormat.format(date!!)
}