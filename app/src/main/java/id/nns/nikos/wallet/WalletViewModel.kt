package id.nns.nikos.wallet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import id.nns.nikos.data.Wallet
import id.nns.nikos.utils.FormattedPrice.getPrice

class WalletViewModel : ViewModel() {

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _totalAmount = MutableLiveData<String>()
    val totalAmount: LiveData<String> get() = _totalAmount

    private val _wallets = MutableLiveData<ArrayList<Wallet>>()
    val wallets: LiveData<ArrayList<Wallet>> get() = _wallets

    private val _isEdited = MutableLiveData<Boolean>()
    val isEdited: LiveData<Boolean> get() = _isEdited

    private val _isDeleted = MutableLiveData<Boolean>()
    val isDeleted: LiveData<Boolean> get() = _isDeleted

    private var dbRef: DatabaseReference = Firebase.database.getReference("wallet")

    fun getWallets() {
        val arrayList = ArrayList<Wallet>()
        var total: Long

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                arrayList.clear()
                total = 0
                for (item in snapshot.children) {
                    val value = item.getValue(Wallet::class.java)
                    if (value != null) {
                        total += value.amount ?: 0
                        arrayList.add(value)
                    }
                }
                _totalAmount.value = getPrice(total)
                _wallets.value = arrayList
            }

            override fun onCancelled(error: DatabaseError) {
                _error.value = error.message
            }
        })
    }

    fun editWallet(id: String, newAmount: Long) {
        dbRef.child("$id/amount").setValue(newAmount)
            .addOnSuccessListener {
                dbRef.child("$id/timestamp").setValue(System.currentTimeMillis() / 1000)
                    .addOnSuccessListener {
                        _isEdited.value = true
                    }
                    .addOnFailureListener {
                        _isEdited.value = false
                        _error.value = it.message
                    }
            }
            .addOnFailureListener {
                _isEdited.value = false
                _error.value = it.message
            }
    }

    fun deleteWallet(id: String) {
        dbRef.child(id).removeValue()
            .addOnSuccessListener {
                _isDeleted.value = true
            }
            .addOnFailureListener {
                _isDeleted.value = false
                _error.value = it.message
            }
    }

}
