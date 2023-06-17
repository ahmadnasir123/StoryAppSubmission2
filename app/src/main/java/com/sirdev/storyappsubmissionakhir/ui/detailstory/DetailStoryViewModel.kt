package com.sirdev.storyappsubmissionakhir.ui.detailstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sirdev.storyappsubmissionakhir.data.preferences.UserStoryPreferences
import com.sirdev.storyappsubmissionakhir.data.response.UserStory

class DetailStoryViewModel(private val preferences: UserStoryPreferences) : ViewModel() {

    private val _stories = MutableLiveData<UserStory>()
    val stories: LiveData<UserStory> = _stories
}