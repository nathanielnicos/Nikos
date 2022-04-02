package id.nns.nikos.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.tasks.CancellationTokenSource
import com.vmadalin.easypermissions.EasyPermissions
import id.nns.nikos.R
import id.nns.nikos.databinding.FragmentMapBinding
import id.nns.nikos.utils.ProgressDialog
import java.util.*

@SuppressLint("MissingPermission")
class MapFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    companion object {
        private const val PERMISSION_LOCATION_REQUEST_CODE = 280222
    }

    private var _binding: FragmentMapBinding? = null
    private val binding: FragmentMapBinding get() = _binding as FragmentMapBinding

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressDialog = ProgressDialog(requireActivity())
        setViewVisibility()

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        binding.btnUpdateLocation.setOnClickListener {
            doSomethingBasedOnExistingPermission()
        }

        binding.btnGotoGmaps.setOnClickListener {
            goToMaps()
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
                binding.tvMessageValue.text = getString(R.string.make_sure_gps_enabled)

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

    private fun doSomethingBasedOnExistingPermission() {
        changeUpdateButtonState(isEnabled = false)
        progressDialog.showLoading()
        showToast("Getting your location...\nPlease wait!")

        if (EasyPermissions.hasPermissions(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            fusedLocationProviderClient.getCurrentLocation(
                LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY,
                CancellationTokenSource().token
            ).addOnSuccessListener { location ->
                changeUpdateButtonState(isEnabled = true)
                progressDialog.dismissLoading()

                if (location != null) {
                    showToast("Updated!")
                    setLocation(location)
                } else {
                    showToast("Turn on your GPS!")
                    openGpsSettings()
                }
            }.addOnFailureListener {
                changeUpdateButtonState(isEnabled = true)
                progressDialog.dismissLoading()

                showToast(it.message.toString())
            }
        } else {
            changeUpdateButtonState(isEnabled = true)
            progressDialog.dismissLoading()

            showToast("No permissions!")
            requestLocationPermission()
        }
    }

    private fun changeUpdateButtonState(isEnabled: Boolean) {
        if (!isEnabled) {
            binding.btnUpdateLocation.isEnabled = false
            binding.btnUpdateLocation.isClickable = false
            binding.btnUpdateLocation.setBackgroundResource(R.drawable.btn_filled_disabled)
        } else {
            binding.btnUpdateLocation.isEnabled = true
            binding.btnUpdateLocation.isClickable = true
            binding.btnUpdateLocation.setBackgroundResource(R.drawable.btn_filled_enabled)
        }
    }

    private fun setLocation(location: Location) {
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

    private fun openGpsSettings() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

    private fun goToMaps() {
        val latitude = binding.tvLatitude.text.toString()
        val longitude = binding.tvLongitude.text.toString()
        val formattedString = String.format(
            Locale.getDefault(),
            "https://maps.google.com/maps?q=loc:$latitude,$longitude"
        )
        val uri = Uri.parse(formattedString)

        if (latitude != "-" && longitude != "-") {
            val intent = Intent(
                Intent.ACTION_VIEW,
                uri
            )
            startActivity(intent)
        } else {
            showToast("No coordinate!")
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
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
