package id.nns.nikos.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import id.nns.nikos.databinding.FragmentMapBinding
import java.util.*

@Suppress("DEPRECATION")
class MapFragment : Fragment() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest

    companion object {
        private const val PERMISSION_ID = 130322
    }

    private var _binding: FragmentMapBinding? = null
    private val binding: FragmentMapBinding get() = _binding as FragmentMapBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        getLastLocation()

        binding.btnUpdateLocation.setOnClickListener {
            getLastLocation()
        }
    }

    private fun checkPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            ),
            PERMISSION_ID
        )
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager =
            activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun getLastLocation() {
        if (checkPermission()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    val location = task.result
                    if (location == null) {
                        getNewLocation()
                    } else {
                        binding.tvLatitude.text = location.latitude.toString()
                        binding.tvLongitude.text = location.longitude.toString()
                        binding.tvLocation.text =
                            getLocationName(location.latitude, location.longitude)
                    }
                }
            } else {
                Toast.makeText(activity, "Please enable your location service!", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            requestPermission()
        }
    }

    private fun getNewLocation() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 2

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Looper.myLooper()?.let { looper ->
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    looper
                )
            }
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            val lastLocation = p0.lastLocation

            binding.tvLatitude.text = lastLocation.latitude.toString()
            binding.tvLongitude.text = lastLocation.longitude.toString()
            binding.tvLocation.text =
                getLocationName(lastLocation.latitude, lastLocation.longitude)
        }
    }

    private fun getLocationName(lat: Double, long: Double): String {
        val geocoder = Geocoder(activity, Locale.getDefault())
        val address = geocoder.getFromLocation(lat, long, 1)

        return address[0].getAddressLine(0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(activity, "You have the permission.", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
