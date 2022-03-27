package id.nns.nikos.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.location.Geocoder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.vmadalin.easypermissions.EasyPermissions
import id.nns.nikos.R
import id.nns.nikos.databinding.FragmentMapBinding

class MapFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    companion object {
        private const val PERMISSION_LOCATION_REQUEST_CODE = 280222
    }

    private var _binding: FragmentMapBinding? = null
    private val binding: FragmentMapBinding get() = _binding as FragmentMapBinding

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewVisibility()

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        binding.btnUpdateLocation.setOnClickListener {
            if (EasyPermissions.hasPermissions(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                    val geocoder = Geocoder(requireContext())
                    val currentLocation = geocoder.getFromLocation(
                        location.latitude,
                        location.longitude,
                        1
                    )

                    binding.tvLatitude.text = location.latitude.toString()
                    binding.tvLongitude.text = location.longitude.toString()
                    binding.tvLocation.text = currentLocation.first().getAddressLine(0)
                }
            } else {
                requestLocationPermission()
            }
        }

        binding.btnClick.setOnClickListener {
            requestLocationPermission()
        }
    }

    private fun setViewVisibility() {
        when {
            EasyPermissions.hasPermissions(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                binding.tvStatusValue.text = getString(R.string.fine_location_permission_granted)
                binding.tvMessageValue.text = getString(R.string.no_value)

                binding.btnClick.isEnabled = false
                binding.btnClick.isClickable = false
                binding.btnClick.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.light_grey
                    )
                )
            }
            EasyPermissions.hasPermissions(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) -> {
                binding.tvStatusValue.text = getString(R.string.coarse_location_permission_granted)
                binding.tvMessageValue.text =
                    getString(R.string.request_fine_location_permission_now)
            }
            else -> {
                binding.tvStatusValue.text = getString(R.string.no_value)
                binding.tvMessageValue.text = getString(R.string.click_the_button)
            }
        }
    }

    private fun requestLocationPermission() {
        EasyPermissions.requestPermissions(
            this,
            "This page needs location permission.",
            PERMISSION_LOCATION_REQUEST_CODE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    @Suppress("DEPRECATION")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        EasyPermissions.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults,
            this
        )
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        requestLocationPermission()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        setViewVisibility()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
