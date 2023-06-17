package com.sirdev.storyappsubmissionakhir.ui.splash

import  androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.sirdev.storyappsubmissionakhir.data.preferences.UserStoryPreferences
import com.sirdev.storyappsubmissionakhir.databinding.ActivitySplashScreenBinding
import com.sirdev.storyappsubmissionakhir.ui.ViewModelFactory
import com.sirdev.storyappsubmissionakhir.ui.login.LoginActivity
import com.sirdev.storyappsubmissionakhir.ui.login.LoginViewModel
import com.sirdev.storyappsubmissionakhir.ui.main.MainActivity


private val Context.dataStore: DataStore<androidx.datastore.preferences.core.Preferences> by preferencesDataStore(name = "settings")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var userViewModel: LoginViewModel
    private lateinit var userToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel = ViewModelProvider(this, ViewModelFactory(UserStoryPreferences.getInstance(dataStore)))[LoginViewModel::class.java]

        userViewModel.dataState().observe(this) {
            userToken = it
        }

        Handler(Looper.getMainLooper()).postDelayed({
            if (userToken.isNotEmpty()) {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("TOKEN", userToken)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 3000)
        supportActionBar?.hide()
    }
}