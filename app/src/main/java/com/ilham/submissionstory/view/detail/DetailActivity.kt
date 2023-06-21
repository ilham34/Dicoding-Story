package com.ilham.submissionstory.view.detail

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.ilham.submissionstory.DateFormatter
import com.ilham.submissionstory.R
import com.ilham.submissionstory.databinding.ActivityDetailBinding
import com.ilham.submissionstory.networking.ListStories
import java.util.*

@Suppress("DEPRECATION")
class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.title_detail_story)

        setDetailStory()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setDetailStory() {
        val detailStory = intent.getParcelableExtra<ListStories>(DETAIL_STORY) as ListStories

        binding.apply {
            tvNameDetail.text = detailStory.name
            tvDescDetail.text = detailStory.description
            tvCreate.text =
                DateFormatter.formatDate(detailStory.createdAt!!, TimeZone.getDefault().id)
            Glide.with(this@DetailActivity)
                .load(detailStory.photoUrl)
                .into(ivStoryDetail)
        }
    }

    companion object {
        const val DETAIL_STORY = "detail_story"
    }
}