package com.sirdev.storyappsubmissionakhir.ui.detailstory


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.sirdev.storyappsubmissionakhir.data.preferences.UserStoryPreferences
import com.sirdev.storyappsubmissionakhir.data.response.ListUserStoriesItem
import com.sirdev.storyappsubmissionakhir.databinding.ActivityDetailStoryBinding
import com.sirdev.storyappsubmissionakhir.ui.ViewModelFactory
import com.sirdev.storyappsubmissionakhir.ui.main.dataStore

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding
    private lateinit var detailStoryViewModel: DetailStoryViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        detailStoryViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserStoryPreferences.getInstance(dataStore))
        )[DetailStoryViewModel::class.java]

        val storyDetail = intent.getParcelableExtra<ListUserStoriesItem>("STORY")
        binding.apply {
            Glide.with(this@DetailStoryActivity).load(storyDetail?.photoUrl).into(detailPhotoIv)
            detailFotoNamaTv.text = storyDetail?.name
            detailDescriptionTv.text = storyDetail?.description
        }

    }

}