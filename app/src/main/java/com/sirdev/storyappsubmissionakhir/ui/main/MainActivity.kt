package com.sirdev.storyappsubmissionakhir.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sirdev.storyappsubmissionakhir.R
import com.sirdev.storyappsubmissionakhir.data.preferences.UserPreferences
import com.sirdev.storyappsubmissionakhir.databinding.ActivityMainBinding
import com.sirdev.storyappsubmissionakhir.ui.MainActivityViewModel
import com.sirdev.storyappsubmissionakhir.ui.adapter.AdapterStories
import com.sirdev.storyappsubmissionakhir.ui.adapter.LoadingAdapterState
import com.sirdev.storyappsubmissionakhir.ui.addstory.AddStoryActivity
import com.sirdev.storyappsubmissionakhir.ui.login.LoginActivity
import com.sirdev.storyappsubmissionakhir.ui.maps.MapsActivity
import kotlinx.coroutines.launch

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var preference: UserPreferences
    private val mainViewModel: MainActivityViewModel by viewModels {
        MainActivityViewModel.ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preference = UserPreferences(this)
        val userToken: String = intent.getStringExtra("TOKEN").toString()
        setRecycleViewMain(userToken)

        mainViewModel.isLoading.observe(this) { loading ->
            showingLoading(loading)
        }
        navigateToAddStoryActivity()
    }

    private fun navigateToAddStoryActivity() {
        binding.fabAddStory.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_logout, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                lifecycleScope.launch {
                    preference.clearUserToken()
                }
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                true
            }
            R.id.options_maps -> {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> {
                true
            }
        }
    }

    private fun showingLoading(isLoading: Boolean) {
        binding.loadingItemBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setRecycleViewMain(userToken: String) {
        val adapterMain = AdapterStories()
        val layoutManager = LinearLayoutManager(this)
        binding.rvStoryList.layoutManager = layoutManager
        binding.rvStoryList.adapter = adapterMain
        binding.rvStoryList.adapter = adapterMain.withLoadStateFooter(
            footer = LoadingAdapterState {
                adapterMain.retry()
            }
        )
        mainViewModel.userStoires.observe(this){
            adapterMain.submitData(lifecycle, it)
        }
    }
}