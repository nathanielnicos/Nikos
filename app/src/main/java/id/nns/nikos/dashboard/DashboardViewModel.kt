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

    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean> get() = _isEmpty

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _pays = MutableLiveData<ArrayList<Pay>>()
    val pays: LiveData<ArrayList<Pay>> get() = _pays

    private var dbRef: DatabaseReference = Firebase.database.getReference("pay")

    fun getPays(count: Int) {
        val arrayList = ArrayList<Pay>()
        val sampleArrayList = ArrayList<Pay>()

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                arrayList.clear()
                sampleArrayList.clear()

                for (item in snapshot.children) {
                    val value = item.getValue(Pay::class.java)
                    if (value != null) {
                        arrayList.add(0, value)
                    }
                }

                when {
                    arrayList.isEmpty() -> {
                        _isEmpty.value = true
                    }
                    arrayList.size < count -> {
                        for (i in 0 until arrayList.size - 1) {
                            sampleArrayList.add(arrayList[i])
                        }
                    }
                    arrayList.size >= count -> {
                        for (i in 0 until count - 1) {
                            sampleArrayList.add(arrayList[i])
                        }
                    }
                }
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
