package id.nns.nikos.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import id.nns.nikos.data.Profile

class ProfileViewModel : ViewModel() {

    private val _profile = MutableLiveData<Profile>()
    val profile: LiveData<Profile> get() = _profile

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> get() = _isSuccess

    fun getProfile() {
        val auth = FirebaseAuth.getInstance()
        _profile.value = Profile(
            name = auth.currentUser?.displayName,
            email = auth.currentUser?.email,
            imgUri = auth.currentUser?.photoUrl
        )
    }

    fun signOut() {
        FirebaseAuth.getInstance().signOut()
        _isSuccess.value = true
    }

}
