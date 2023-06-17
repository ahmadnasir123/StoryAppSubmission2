package com.sirdev.storyappsubmissionakhir.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sirdev.storyappsubmissionakhir.data.api.ApiConfig
import com.sirdev.storyappsubmissionakhir.data.response.GeneralResponseError
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val client = ApiConfig.getApiService()

    private  val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun userRegister(username: String, emailUser: String, password: String) {
        _isLoading.value = true
        client.userRegister(username, emailUser, password).enqueue(object : Callback<GeneralResponseError> {
            override fun onResponse(
                call: Call<GeneralResponseError>,
                response: Response<GeneralResponseError>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _message.value = response.body()?.message
                } else {
                    _message.value = response.message()
                }
            }

            override fun onFailure(call: Call<GeneralResponseError>, t: Throwable) {
                _isLoading.value = false
                _message.value = t.message
            }
        })
    }
}