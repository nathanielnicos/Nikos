package id.nns.nikos.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import id.nns.nikos.home.HomeActivity
import id.nns.nikos.utils.ProgressDialog
import id.nns.nikos.R
import id.nns.nikos.databinding.ActivityMainBinding

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Nikos_NoActionBar)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog = ProgressDialog(this)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.init(this)

        binding.btnLogin.setOnClickListener {
            progressDialog.showLoading()
            viewModel.signIn(this)
        }

        observe()
    }

    private fun observe() {
        viewModel.isLogin.observe(this) {
            if (it) {
                goToHome()
            }
        }

        viewModel.isSuccess.observe(this) {
            if (it) {
                progressDialog.dismissLoading()
                goToHome()
            }
        }

        viewModel.message.observe(this) {
            progressDialog.dismissLoading()
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        viewModel.onResult(this, requestCode, data)
    }

    override fun onStart() {
        super.onStart()

        viewModel.checkUserLogin()
    }

    private fun goToHome() {
        Intent(this, HomeActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }

}
