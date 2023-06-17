package com.sirdev.storyappsubmissionakhir.data.Utils

import android.content.Context
import com.sirdev.storyappsubmissionakhir.data.api.ApiConfig
import com.sirdev.storyappsubmissionakhir.data.preferences.UserPreferences
import com.sirdev.storyappsubmissionakhir.data.response.StoryRepository

object Injection {
    fun provideStoryRepository(context: Context) : StoryRepository {
        val apiService = ApiConfig.getApiService()
        val preference = UserPreferences(context)
        return StoryRepository(apiService, preference)
    }
}