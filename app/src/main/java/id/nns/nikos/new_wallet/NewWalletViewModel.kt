package id.nns.nikos.new_wallet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import id.nns.nikos.data.Icon
import id.nns.nikos.data.Wallet

class NewWalletViewModel : ViewModel() {

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> get() = _isSuccess

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _icons = MutableLiveData<ArrayList<Icon>>()
    val icons: LiveData<ArrayList<Icon>> get() = _icons

    fun showWalletIcon() {
        val iconList = ArrayList<Icon>()
        Firebase.database.getReference("icon")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    iconList.clear()

                    for (item in snapshot.children) {
                        val value = item.getValue(Icon::class.java)
                        if (value != null) {
                            iconList.add(value)
                        }
                    }

                    _icons.value = iconList
                }

                override fun onCancelled(error: DatabaseError) {
                    _error.value = error.message
                }
            })
    }

    fun setWallet(logoUrl: String, amount: String) {
        val ref = Firebase.database.getReference("wallet").push()

        ref.setValue(
            Wallet(
                id = ref.key.toString(),
                logoUrl = logoUrl,
                amount = amount.toLong(),
                timestamp = System.currentTimeMillis() / 1000
            )
        ).addOnSuccessListener {
            _isSuccess.value = true
        }.addOnFailureListener {
            _isSuccess.value = false
            _error.value = it.message
        }
    }

}
