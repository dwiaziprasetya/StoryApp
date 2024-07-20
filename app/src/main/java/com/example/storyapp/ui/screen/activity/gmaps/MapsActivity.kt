package com.example.storyapp.ui.screen.activity.gmaps

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityMapsBinding
import com.example.storyapp.helper.DialogHelper
import com.example.storyapp.helper.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val viewModel by viewModels<MapsViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        setMapStyle()

        val dicodingSpace = LatLng(-6.8957643, 107.6338462)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dicodingSpace, 5f))

        mMap.uiSettings.isZoomControlsEnabled = true
        viewModel.stories.observe(this) { response ->
            if (response.error) {
                DialogHelper.showErrorDialog(
                    this,
                    "Stories Failed",
                    response.message,
                )
            } else {
                response.listStory.forEach { story ->
                    val latLng = LatLng(story.lat, story.lon)
                    mMap.addMarker(
                        MarkerOptions()
                            .position(latLng)
                            .title(story.name)
                            .snippet(story.description)
                    )
                }
            }
        }
        viewModel.isLoading.observe(this) {
            if (it) {
                Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        this,
                        R.raw.map_style
                    )
                )
            if (!success) Log.e(TAG, "Style parsing failed")
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", e)
        }
    }

    companion object {
        private const val TAG = "MapsActivity"
    }
}