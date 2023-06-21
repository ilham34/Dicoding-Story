package com.ilham.submissionstory.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ilham.submissionstory.databinding.ItemRowStoriesBinding
import com.ilham.submissionstory.networking.ListStories
import com.ilham.submissionstory.view.detail.DetailActivity

class StoriesAdapter :
    PagingDataAdapter<ListStories, StoriesAdapter.ListViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemRowStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    class ListViewHolder(private var binding: ItemRowStoriesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(listStories: ListStories) {
            binding.apply {
                tvTitle.text = listStories.name
                Glide.with(itemView.context)
                    .load(listStories.photoUrl)
                    .into(ivStory)
                tvDescItem.text = listStories.description
                cardView.setOnClickListener {

                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            Pair(ivStory, "photo"),
                            Pair(tvTitle, "name"),
                            Pair(tvDescItem, "description"),
                        )

                    val moveDetailStories = Intent(itemView.context, DetailActivity::class.java)
                    moveDetailStories.putExtra(DetailActivity.DETAIL_STORY, listStories)

                    itemView.context.startActivity(moveDetailStories, optionsCompat.toBundle())

                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStories>() {

            override fun areItemsTheSame(oldItem: ListStories, newItem: ListStories): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStories, newItem: ListStories): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }
}