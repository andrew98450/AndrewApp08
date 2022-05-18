package com.andrew.andrewapp_08

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.andrew.andrewapp_08.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.PolylineOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var REQUEST_CODE = 1

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isEmpty()) return
        when(requestCode){
            REQUEST_CODE -> {
                for(result in grantResults) {
                    if(result != PackageManager.PERMISSION_GRANTED)
                        finish()
                    else {
                        val mapFragment = supportFragmentManager
                            .findFragmentById(R.id.map) as SupportMapFragment
                        mapFragment.getMapAsync(this)
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkPermission(Manifest.permission.INTERNET, REQUEST_CODE)
        checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, REQUEST_CODE)
        checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_CODE)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun checkPermission(permission: String, requestCode: Int) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED) {
                // Requesting the permission
                requestPermissions(arrayOf(permission), requestCode)
            } else {
                Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show()
            }
        }
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "PERMISSION_DENIAL", Toast.LENGTH_LONG).show()
            return
        }

        mMap = googleMap
        mMap.isMyLocationEnabled = true

        val marker = MarkerOptions()
        marker.position(LatLng(25.033611,121.565000))
        marker.title("Taipei 101")
        marker.draggable(true)
        mMap.addMarker(marker)
        marker.position(LatLng(25.047924,121.517081))
        marker.title("Taipei Station")
        marker.draggable(true)
        mMap.addMarker(marker)

        val polylineOptions = PolylineOptions()
        polylineOptions.add(LatLng(25.033611,121.565000))
        polylineOptions.add(LatLng(25.032728,121.565137))
        polylineOptions.add(LatLng(25.047924,121.517081))
        polylineOptions.color(Color.BLUE)
        val polyLine = mMap.addPolyline(polylineOptions)
        polyLine.width = 10f
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(25.034,121.545), 13f))
    }
}