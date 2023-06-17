package com.sirdev.storyappsubmissionakhir.ui.addstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.sirdev.storyappsubmissionakhir.data.api.ApiConfig
import com.sirdev.storyappsubmissionakhir.data.preferences.UserStoryPreferences
import com.sirdev.storyappsubmissionakhir.data.response.GeneralResponseError
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddStoryViewModel(private val preference: UserStoryPreferences) : ViewModel() {

    private val _isLoading  = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _messageError = MutableLiveData<String>()
    val messageError: LiveData<String> = _messageError

    private val client = ApiConfig.getApiService()

    fun postNewStory(userToken: String, file: MultipartBody.Part, description: RequestBody) {
        _isLoading.value = true
        client.uploadNewStory(token = "Bearer $userToken", file, description).enqueue(object : Callback<GeneralResponseError>{
            override fun onResponse(
                call: Call<GeneralResponseError>,
                response: Response<GeneralResponseError>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _messageError.value = response.body()?.message
                } else {
                    _messageError.value = response.message().toString()
                }
            }

            override fun onFailure(call: Call<GeneralResponseError>, t: Throwable) {
                _isLoading.value = false
                _messageError.value = t.message
            }

        })
    }

    fun dataState(): LiveData<String> {
        return preference.getTokenKey().asLiveData()
    }
}