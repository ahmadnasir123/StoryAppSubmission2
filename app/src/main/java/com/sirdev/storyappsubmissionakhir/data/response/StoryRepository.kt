package com.sirdev.storyappsubmissionakhir.data.response

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.sirdev.storyappsubmissionakhir.data.StoryPagingSource
import com.sirdev.storyappsubmissionakhir.data.api.ApiService
import com.sirdev.storyappsubmissionakhir.data.preferences.UserPreferences

class StoryRepository(private val apiService: ApiService, private val preference: UserPreferences) {
    val tokenUser = preference.getUserToken()

    fun getAllUserStories(): LiveData<PagingData<ListUserStoriesItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, tokenUser.toString())
            }
        ).liveData
    }
}