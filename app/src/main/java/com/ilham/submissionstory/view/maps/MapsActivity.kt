package com.ilham.submissionstory.view.maps

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.ilham.submissionstory.Helper
import com.ilham.submissionstory.R
import com.ilham.submissionstory.databinding.ActivityMapsBinding
import com.ilham.submissionstory.model.Result
import com.ilham.submissionstory.model.UserPreference
import com.ilham.submissionstory.networking.ListStories
import com.ilham.submissionstory.view.ViewModelFactory
import com.ilham.submissionstory.view.main.MainViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var helper: Helper
    private lateinit var progressBar: View

    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory(UserPreference.getInstance(dataStore), this)
    }

    private val mapViewModel by viewModels<MapsViewModel> {
        ViewModelFactory(UserPreference.getInstance(dataStore), this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        helper = Helper()
        progressBar = binding.progressBar
        supportActionBar?.title = getString(R.string.maps)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        getMyLocation()
        setMapStyle()
        getLocationOnMap()
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, getString(R.string.parsing_style_failed))
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, getString(R.string.error_msg_maps), exception)
        }
    }

    private fun getLocationOnMap() {
        lifecycleScope.launchWhenCreated {
            mainViewModel.getUser().collect {
                mapViewModel.getStoriesWithMaps(it.token).observe(this@MapsActivity) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> {
                                helper.showLoading(true, progressBar)
                            }
                            is Result.Success -> {
                                helper.showLoading(false, progressBar)
                                val location = result.data.story
                                pinMarker(location)
                            }
                            is Result.Error -> {
                                helper.showLoading(false, progressBar)
                                showText(getString(R.string.failed_map, result.error))
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showText(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private val boundsBuilder = LatLngBounds.Builder()
    private fun pinMarker(location: List<ListStories>) {
        location.forEach { item ->
            if (item.lat != null && item.lon != null) {
                val marker = LatLng(item.lat, item.lon)
                mMap.addMarker(
                    MarkerOptions()
                        .position(marker)
                        .title(item.name)
                        .snippet("Description : ${item.description}")
                        .icon(
                            BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_GREEN
                            )
                        )
                )
                boundsBuilder.include(marker)
            }

            val bounds: LatLngBounds = boundsBuilder.build()
            mMap.animateCamera(
                CameraUpdateFactory.newLatLngBounds(
                    bounds,
                    resources.displayMetrics.widthPixels,
                    resources.displayMetrics.heightPixels,
                    300
                )
            )
        }
    }

    companion object {
        private const val TAG = "MapsActivity"
    }
}