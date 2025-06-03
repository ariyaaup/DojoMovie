package com.example.dojomovie

import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.dojomovie.databinding.ActivityMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapBinding

    private lateinit var etSearch: EditText
    private lateinit var btnSearch: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        etSearch = findViewById(R.id.etSearch)
        btnSearch = findViewById(R.id.btnSearch)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        setupFindLocation()
    }

    fun setupFindLocation() {

        btnSearch.setOnClickListener {
            var query = etSearch.text.toString()

            if (query.isEmpty())
                return@setOnClickListener

            // Geocoder
            var geoCoder = Geocoder(this@MapActivity)

            geoCoder.getFromLocationName(query, 1, Geocoder.GeocodeListener {
                if (it.size > 0) {
                    var result = it[0]

                    var coord = LatLng(result.latitude, result.longitude)

                    runOnUiThread {
                        mMap.clear()
                        mMap.addMarker(MarkerOptions().position(coord).title(query))
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coord, 5.0f))
                    }
                }
            })

        }
    }

    fun backPressed(view: View) {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }


}