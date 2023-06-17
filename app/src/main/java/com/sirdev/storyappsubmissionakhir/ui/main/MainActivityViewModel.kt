package com.sirdev.storyappsubmissionakhir.ui

import android.content.Context
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sirdev.storyappsubmissionakhir.data.Utils.Injection
import com.sirdev.storyappsubmissionakhir.data.response.ListUserStoriesItem
import com.sirdev.storyappsubmissionakhir.data.response.StoryRepository

class MainActivityViewModel(userStoryRepository: StoryRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val userStoires: LiveData<PagingData<ListUserStoriesItem>> = userStoryRepository.getAllUserStories().cachedIn(viewModelScope)

    class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainActivityViewModel(Injection.provideStoryRepository(context)) as T
            }
            throw IllegalArgumentException("UNKNOWN ViewModel class")
        }
    }
}