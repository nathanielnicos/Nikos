package id.nns.nikos.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import id.nns.nikos.data.Product

class DetailViewModel : ViewModel() {

    private val _pay = MutableLiveData<Product>()
    val product: LiveData<Product> get() = _pay

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> get() = _isSuccess

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun getPay(id: String) {
        val dbRef = Firebase.database.getReference("pay/$id")

        dbRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.getValue(Product::class.java)
                data.let {
                    _pay.value = it
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _error.value = error.message
            }
        })
    }

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