package com.sirdev.storyappsubmissionakhir.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sirdev.storyappsubmissionakhir.data.preferences.UserStoryPreferences
import com.sirdev.storyappsubmissionakhir.ui.addstory.AddStoryViewModel
import com.sirdev.storyappsubmissionakhir.ui.detailstory.DetailStoryViewModel
import com.sirdev.storyappsubmissionakhir.ui.login.LoginViewModel
import com.sirdev.storyappsubmissionakhir.ui.maps.MapsViewModel

class ViewModelFactory(private val preference: UserStoryPreferences) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return when {
            modelClass.isAssignableFrom(DetailStoryViewModel::class.java) -> {
                DetailStoryViewModel(preference) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(preference) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(preference) as T
            }
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(preference) as T
            }
            else -> throw IllegalArgumentException("Uknown ViewModel class: " + modelClass.name)
        }
    }
}