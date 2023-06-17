package com.sirdev.storyappsubmissionakhir.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sirdev.storyappsubmissionakhir.databinding.StoryItemBinding
import com.sirdev.storyappsubmissionakhir.ui.detailstory.DetailStoryActivity
import com.sirdev.storyappsubmissionakhir.data.response.ListUserStoriesItem

class AdapterStories : PagingDataAdapter<ListUserStoriesItem, AdapterStories.ViewHolder>(CALLBACK_DIFF) {
    class ViewHolder(private val binding: StoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ListUserStoriesItem) {
            Glide.with(itemView.context).load(data.photoUrl).into(binding.photoVw)
            binding.nameVw.text = data.name
            itemView.setOnClickListener {
                val intent = Intent(it.context, DetailStoryActivity::class.java)
                intent.putExtra("STORY", data)
                it.context.startActivity(intent)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterStories.ViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    companion object {
        val CALLBACK_DIFF = object : DiffUtil.ItemCallback<ListUserStoriesItem>() {
            override fun areItemsTheSame(oldItem: ListUserStoriesItem, newItem: ListUserStoriesItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(
                oldItem: ListUserStoriesItem,
                newItem: ListUserStoriesItem
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}