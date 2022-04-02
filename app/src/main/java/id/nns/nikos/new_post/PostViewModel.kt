package id.nns.nikos.new_post

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import id.nns.nikos.data.Product

class PostViewModel : ViewModel() {

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> get() = _isSuccess

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun savePost(uri: Uri, product: String, details: String, price: Long) {
        val timestamp = System.currentTimeMillis() / 1000
        val storageRef = Firebase.storage.reference.child("pay/$timestamp")

        storageRef.putFile(uri)
            .addOnSuccessListener {
                storageRef.downloadUrl
                    .addOnSuccessListener { imgUrl ->
                        // Save to realtime database
                        val realtimeRef = Firebase.database.getReference("pay").push()

                        realtimeRef.setValue(
                            Product(
                                id = realtimeRef.key,
                                imgUrl = imgUrl.toString(),
                                favorite = false,
                                product = product,
                                price = price,
                                timestamp = timestamp,
                                detail = details
                            )
                        ).addOnSuccessListener {
                            _isSuccess.value = true
                        }.addOnFailureListener {
                            _isSuccess.value = false
                            _error.value = it.message
                        }
                    }
                    .addOnFailureListener {
                        _isSuccess.value = false
                        _error.value = it.message
                    }
            }
            .addOnFailureListener {
                _isSuccess.value = false
                _error.value = it.message
            }
    }

}
