package id.nns.nikos.map

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import id.nns.nikos.databinding.FragmentMapBinding

class MapFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    companion object {
        private const val PERMISSION_LOCATION_REQUEST_CODE = 280222
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

        setViewVisibility()

        binding.btnRequestPermission.setOnClickListener {
            requestLocationPermission()
        }
    }

    private fun hasLocationPermission() = EasyPermissions.hasPermissions(
        requireContext(),
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

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
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(requireContext()).build().show()
        } else {
            requestLocationPermission()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Toast.makeText(
            requireContext(),
            "Permission granted!",
            Toast.LENGTH_SHORT
        ).show()

        setViewVisibility()
    }

    private fun setViewVisibility() {
        if (hasLocationPermission()) {
            binding.tvPermissionGranted.visibility = View.VISIBLE
            binding.btnRequestPermission.visibility = View.GONE
        } else {
            binding.tvPermissionGranted.visibility = View.GONE
            binding.btnRequestPermission.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
