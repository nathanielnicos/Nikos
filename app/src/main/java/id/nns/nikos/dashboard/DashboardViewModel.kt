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
import id.nns.nikos.data.Pay

class DashboardViewModel : ViewModel() {

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _pays = MutableLiveData<ArrayList<Pay>>()
    val pays: LiveData<ArrayList<Pay>> get() = _pays

    private var dbRef: DatabaseReference = Firebase.database.getReference("pay")

    fun getPays(count: Int) {
        val arrayList = ArrayList<Pay>()

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                arrayList.clear()
                for (item in snapshot.children) {
                    val value = item.getValue(Pay::class.java)
                    if (value != null) {
                        arrayList.add(0, value)
                    }
                }
                _pays.value = arrayList.take(count) as ArrayList<Pay>
            }

            override fun onCancelled(error: DatabaseError) {
                _error.value = error.message
            }
        })
    }

    fun updateFavoriteState(id: String, favorite: Boolean) {
        dbRef.child("$id/favorite").setValue(!favorite)
            .addOnFailureListener {
                _error.value = it.message
            }
    }

}
