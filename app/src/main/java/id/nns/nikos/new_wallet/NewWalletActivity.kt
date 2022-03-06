package id.nns.nikos.new_wallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import id.nns.nikos.databinding.ActivityNewWalletBinding

class NewWalletActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewWalletBinding
    private lateinit var viewModel: NewWalletViewModel
    private lateinit var adapter: NewWalletAdapter
    private var logoUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewWalletBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = NewWalletAdapter { name, iconUrl ->
            title = "$name is selected."
            logoUrl = iconUrl
        }

        binding.rvWalletIcon.layoutManager = GridLayoutManager(this, 4)
        binding.rvWalletIcon.adapter = adapter

        viewModel = ViewModelProvider(this)[NewWalletViewModel::class.java]
        viewModel.showWalletIcon()

        binding.fabSaveNewWallet.setOnClickListener {
            val amount = binding.etAmount.text.toString().trim()

            when {
                logoUrl == null -> {
                    Toast.makeText(this, "Please select a wallet!", Toast.LENGTH_SHORT)
                        .show()
                }
                amount.isBlank() -> {
                    binding.etAmount.error = "Amount field is empty!"
                }
                else -> {
                    viewModel.setWallet(
                        logoUrl = logoUrl.toString(),
                        amount = amount
                    )
                }
            }
        }

        observe()
    }

    private fun observe() {
        viewModel.isSuccess.observe(this) { isSuccess ->
            if (isSuccess) {
                finish()
            }
        }

        viewModel.error.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }

        viewModel.icons.observe(this) { icons ->
            adapter.setIcon(icons)
        }
    }

}
