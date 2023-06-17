package com.sirdev.storyappsubmissionakhir.ui.login

import androidx.lifecycle.*
import com.sirdev.storyappsubmissionakhir.data.api.ApiConfig
import com.sirdev.storyappsubmissionakhir.data.preferences.UserStoryPreferences
import com.sirdev.storyappsubmissionakhir.data.response.LoginUserResult
import com.sirdev.storyappsubmissionakhir.data.response.ResponseLoginUser
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginViewModel(private val preference: UserStoryPreferences) : ViewModel() {
    private val _dataLogin = MutableLiveData<LoginUserResult>()
    val dataLogin: LiveData<LoginUserResult> = _dataLogin

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val client = ApiConfig.getApiService()

    private val _messageResponse = MutableLiveData<String>()
    val messageResponse: LiveData<String> = _messageResponse

    fun dataState(): LiveData<String> {
        return  preference.getTokenKey().asLiveData()
    }

    fun userLogin(emailUser: String, password: String) {
        _isLoading.value = true
        client.userLogin(emailUser, password).enqueue(object : Callback<ResponseLoginUser> {
            override fun onResponse(
                call: Call<ResponseLoginUser>,
                response: Response<ResponseLoginUser>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _dataLogin.value = response.body()?.loginResult
                    _messageResponse.value = response.body()?.message
                    viewModelScope.launch {
                        preference.saveTokenKey(response.body()?.loginResult?.token.toString())
                    }
                } else {
                    _messageResponse.value = response.message()
                }
            }

            override fun onFailure(call: Call<ResponseLoginUser>, t: Throwable) {
                _isLoading.value = false
                _messageResponse.value = t.message
            }

        })
    }
}