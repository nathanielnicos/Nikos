package id.nns.nikos.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DetailViewModel : ViewModel() {

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> get() = _isSuccess

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun savePay(id: String, product: String, detail: String) {
        val dbRef = Firebase.database.getReference("pay/$id")

        dbRef.child("product")
            .setValue(product)
            .addOnSuccessListener {
                dbRef.child("detail")
                    .setValue(detail)
                    .addOnSuccessListener {
                        _isSuccess.value = true
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