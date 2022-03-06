package id.nns.nikos.main

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import id.nns.nikos.R

class MainViewModel : ViewModel() {

    private val _isLogin = MutableLiveData<Boolean>()
    val isLogin: LiveData<Boolean> get() = _isLogin

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> get() = _isSuccess

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private val signInRequestCode = 1

    fun init(activity: Activity) {
        auth = Firebase.auth

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(activity, gso)
    }

    private fun firebaseAuthWithGoogle(activity: Activity, idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    _isSuccess.value = true
                } else {
                    _message.value = task.exception?.message
                }
            }
    }

    fun signIn(activity: Activity) {
        googleSignInClient.signInIntent.also {
            activity.startActivityForResult(it, signInRequestCode)
        }
    }

    fun checkUserLogin() {
        _isLogin.value = auth.currentUser != null
    }

    fun onResult(activity: Activity, requestCode: Int, data: Intent?) {
        if (requestCode == signInRequestCode) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)
                account.idToken?.let {
                    firebaseAuthWithGoogle(
                        activity,
                        it
                    )
                }
            } catch (e: ApiException) {
                _message.value = e.message
            }
        }
    }

}
