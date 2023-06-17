package com.sirdev.storyappsubmissionakhir.ui.maps

import android.content.ContentValues.TAG
import android.content.Context
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.sirdev.storyappsubmissionakhir.R
import com.sirdev.storyappsubmissionakhir.data.preferences.UserStoryPreferences
import com.sirdev.storyappsubmissionakhir.databinding.ActivityMapsBinding
import com.sirdev.storyappsubmissionakhir.ui.ViewModelFactory



private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var mapsViewModel: MapsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mapsViewModel = ViewModelProvider(this, ViewModelFactory(UserStoryPreferences.getInstance(dataStore)))[MapsViewModel::class.java]


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mapsViewModel.dataState().observe(this) {userToken ->
            mapsViewModel.getAllStoriesOnMapLocation(userToken)
        }

        mapsViewModel.stories.observe(this) { stories ->
            for (location in stories) {
                val locat = LatLng(location.lat, location.lon)
                mMap.addMarker(MarkerOptions().position(locat).title("${location.name} marker"))
            }
            val moveLocations = LatLng(stories[0].lat, stories[0].lon)
            mMap.moveCamera(CameraUpdateFactory.newLatLng(moveLocations))
        }

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled =true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        try {
            val success = mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

}