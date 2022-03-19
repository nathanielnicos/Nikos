package id.nns.nikos.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import id.nns.nikos.data.Product

class DashboardViewModel : ViewModel() {

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _favoriteProduct = MutableLiveData<ArrayList<Product>>()
    val favoriteProduct: LiveData<ArrayList<Product>> get() = _favoriteProduct

    private val _recentProduct = MutableLiveData<ArrayList<Product>>()
    val recentProduct: LiveData<ArrayList<Product>> get() = _recentProduct

    private var dbRef: DatabaseReference = Firebase.database.getReference("pay")

    fun getImages(count: Int) {
        val arrayList = ArrayList<Product>()

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                arrayList.clear()

                snapshot.children.reversed().forEach { data ->
                    val value = data.getValue(Product::class.java)
                    if (value != null && value.favorite && arrayList.size < count) {
                        arrayList.add(value)
                    }
                }

                _favoriteProduct.value = arrayList
            }

            override fun onCancelled(error: DatabaseError) {
                _error.value = error.message
            }
        })
    }

    fun getPays(count: Int) {
        val arrayList = ArrayList<Product>()

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                arrayList.clear()

                snapshot.children.reversed().take(count).forEach { data ->
                    val value = data.getValue(Product::class.java)
                    if (value != null) {
                        arrayList.add(value)
                    }
                }

                _recentProduct.value = arrayList
            }

            override fun onCancelled(error: DatabaseError) {
                _error.value = error.message
            }
        })
    }

    fun updateFavoriteState(id: String, isFavorite: Boolean) {
        dbRef.child("$id/favorite").setValue(isFavorite)
            .addOnFailureListener {
                _error.value = it.message
            }
    }

}
