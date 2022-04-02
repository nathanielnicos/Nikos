package id.nns.nikos.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.firebase.database.FirebaseDatabase
import com.vmadalin.easypermissions.EasyPermissions
import id.nns.nikos.R
import id.nns.nikos.data.Location
import id.nns.nikos.utils.SettingPreference

@SuppressLint("MissingPermission")
class MapViewModel : ViewModel() {

    private val _location = MutableLiveData<Location>()
    val location: LiveData<Location> get() = _location

    private val _isDismissLoading = MutableLiveData<Boolean>()
    val isDismissLoading: LiveData<Boolean> get() = _isDismissLoading

    private val _isGpsDisabled = MutableLiveData<Boolean>()
    val isGpsDisabled: LiveData<Boolean> get() = _isGpsDisabled

    private val _isButtonClickDisabled = MutableLiveData<Boolean>()
    val isButtonClickDisabled: LiveData<Boolean> get() = _isButtonClickDisabled

    private val _isNeedLocationPermission = MutableLiveData<Boolean>()
    val isNeedLocationPermission: LiveData<Boolean> get() = _isNeedLocationPermission

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    private val _status = MutableLiveData<String>()
    val status: LiveData<String> get() = _status

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    fun setViewVisibility(context: Context) {
        when {
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                _status.value = context.getString(R.string.fine_location_permission_granted)
                _message.value = context.getString(R.string.make_sure_gps_enabled)
                _isButtonClickDisabled.value = true
            }
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) -> {
                _status.value = context.getString(R.string.coarse_location_permission_granted)
                _message.value =
                    context.getString(R.string.request_fine_location_permission_now)
            }
            else -> {
                _status.value = context.getString(R.string.no_value)
                _message.value = context.getString(R.string.click_the_button)
            }
        }
    }

    fun doSomethingBasedOnExistingPermission(context: Context) {
        if (EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(context)

            fusedLocationProviderClient.getCurrentLocation(
                LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY,
                CancellationTokenSource().token
            ).addOnSuccessListener { location ->
                _isDismissLoading.value = true

                if (location != null) {
                    val settingPref = SettingPreference(context)
                    val isShareLocEnabled = settingPref.getShareLocState()

                    setLocation(context, location, isShareLocEnabled)

                    _toastMessage.value = "Updated!"
                } else {
                    _toastMessage.value = "Turn on your GPS!"
                    _isGpsDisabled.value = true
                }
            }.addOnFailureListener {
                _isDismissLoading.value = true
                _toastMessage.value = it.message.toString()
            }
        } else {
            _isDismissLoading.value = true
            _isNeedLocationPermission.value = true
            _toastMessage.value = "No permissions!"
        }
    }

    fun requestLocationPermission(fragment: Fragment) {
        EasyPermissions.requestPermissions(
            fragment,
            "This page needs location permission.",
            MapFragment.PERMISSION_LOCATION_REQUEST_CODE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    private fun setLocation(
        context: Context,
        location: android.location.Location,
        isShareLocEnabled: Boolean
    ) {
        val geocoder = Geocoder(context)
        val currentLocation = geocoder.getFromLocation(
            location.latitude,
            location.longitude,
            1
        )

        val latitude = location.latitude
        val longitude = location.longitude
        val address = currentLocation.first().getAddressLine(0)

        _location.value = Location(
            lat = latitude,
            long = longitude,
            address = address
        )

        if (isShareLocEnabled) {
            shareLocation(latitude, longitude, address)
        }
    }

    private fun shareLocation(lat: Double, long: Double, address: String) {
        val ref = FirebaseDatabase.getInstance().getReference("location")

        ref.setValue(
            Location(
                id = ref.key,
                lat = lat,
                long = long,
                address = address,
                timestamp = System.currentTimeMillis() / 1000
            )
        )
    }

}