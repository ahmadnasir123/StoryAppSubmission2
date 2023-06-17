package com.sirdev.storyappsubmissionakhir.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.sirdev.storyappsubmissionakhir.data.api.ApiConfig
import com.sirdev.storyappsubmissionakhir.data.preferences.UserStoryPreferences
import com.sirdev.storyappsubmissionakhir.data.response.ListUserStoriesItem
import com.sirdev.storyappsubmissionakhir.data.response.ResponseUserStories
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel(private val preference: UserStoryPreferences) : ViewModel() {

    private val _stories = MutableLiveData<List<ListUserStoriesItem>>()
    val stories: LiveData<List<ListUserStoriesItem>> = _stories

    private val _messageError = MutableLiveData<String>()
    val messageError: LiveData<String> = _messageError

    private val client = ApiConfig.getApiService()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getAllStoriesOnMapLocation(userToken: String) {
        _isLoading.value = true
        client.getAllUserStoriesWithLocation("Bearer $userToken", 1).enqueue(object : Callback<ResponseUserStories> {
            override fun onResponse(
                call: Call<ResponseUserStories>,
                response: Response<ResponseUserStories>
            ) {
                _isLoading.value = false
                _messageError.value = response.body()?.message
                if (response.isSuccessful){
                    _stories.value = response.body()?.listStory
                }
            }

            override fun onFailure(call: Call<ResponseUserStories>, t: Throwable) {
                _isLoading.value =false
                _messageError.value = t.message
            }
        })
    }

    fun dataState(): LiveData<String> {
        return preference.getTokenKey().asLiveData()
    }
}