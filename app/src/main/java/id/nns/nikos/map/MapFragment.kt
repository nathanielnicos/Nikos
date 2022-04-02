package id.nns.nikos.map

import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.vmadalin.easypermissions.EasyPermissions
import id.nns.nikos.R
import id.nns.nikos.databinding.FragmentMapBinding
import id.nns.nikos.utils.ProgressDialog
import java.util.*

class MapFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    companion object {
        const val PERMISSION_LOCATION_REQUEST_CODE = 280222
    }

    private var _binding: FragmentMapBinding? = null
    private val binding: FragmentMapBinding get() = _binding as FragmentMapBinding

    private lateinit var viewModel: MapViewModel
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

        viewModel = ViewModelProvider(this)[MapViewModel::class.java]
        progressDialog = ProgressDialog(requireActivity())

        viewModel.setViewVisibility(requireContext())

        binding.btnUpdateLocation.setOnClickListener {
            changeUpdateButtonState(isEnabled = false)
            progressDialog.showLoading()
            showToast("Getting your location...\nPlease wait!")

            viewModel.doSomethingBasedOnExistingPermission(requireContext())
        }

        binding.btnGotoGmaps.setOnClickListener {
            goToMaps()
        }

        binding.btnClick.setOnClickListener {
            viewModel.requestLocationPermission(this)
        }

        observe()
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

    private fun observe() {
        viewModel.isDismissLoading.observe(viewLifecycleOwner) { isDismissLoading ->
            if (isDismissLoading) {
                changeUpdateButtonState(isEnabled = true)
                progressDialog.dismissLoading()
            }
        }

        viewModel.toastMessage.observe(viewLifecycleOwner) { toastMessage ->
            showToast(toastMessage)
        }

        viewModel.isNeedLocationPermission.observe(viewLifecycleOwner) { isNeedLocationPermission ->
            if (isNeedLocationPermission) {
                viewModel.requestLocationPermission(this)
            }
        }

        viewModel.isGpsDisabled.observe(viewLifecycleOwner) { isGpsDisabled ->
            if (isGpsDisabled) {
                openGpsSettings()
            }
        }

        viewModel.isButtonClickDisabled.observe(viewLifecycleOwner) { isButtonClickDisabled ->
            if (isButtonClickDisabled) {
                binding.btnClick.isEnabled = false
                binding.btnClick.isClickable = false
                binding.btnClick.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.light_grey
                    )
                )
            }
        }

        viewModel.status.observe(viewLifecycleOwner) { status ->
            binding.tvStatusValue.text = status
        }

        viewModel.message.observe(viewLifecycleOwner) { message ->
            binding.tvMessageValue.text = message
        }

        viewModel.location.observe(viewLifecycleOwner) { location ->
            binding.tvLatitude.text = location.lat.toString()
            binding.tvLongitude.text = location.long.toString()
            binding.tvLocation.text = location.address
        }
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
        viewModel.requestLocationPermission(this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        viewModel.setViewVisibility(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
