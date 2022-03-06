package id.nns.nikos.wallet

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.nns.nikos.databinding.FragmentWalletBinding
import id.nns.nikos.new_wallet.NewWalletActivity
import id.nns.nikos.utils.SettingPreference

class WalletFragment : Fragment() {

    private var _binding: FragmentWalletBinding? = null
    private val binding: FragmentWalletBinding get() = _binding as FragmentWalletBinding

    private lateinit var adapter: WalletAdapter
    private lateinit var viewModel: WalletViewModel

    private var getId: String? = null
    private var getOldAmount: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWalletBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[WalletViewModel::class.java]
        viewModel.getWallets()

        val settingPref = context?.let { SettingPreference(it) }
        val keyWallet = settingPref?.getWalletEditAndDeleteFeatures() ?: "long_press"

        adapter = WalletAdapter(
            keyWallet = keyWallet,
            getData = { id, oldAmount ->
                getId = id
                getOldAmount = oldAmount
            },
            editWallet = { id, oldAmount ->
                binding.clEditWallet.visibility = View.VISIBLE
                binding.etAmount.setText(oldAmount.toString())

                binding.ibSave.setOnClickListener {
                    val newAmount = binding.etAmount.text.toString().trim()

                    if (newAmount.isNotBlank()) {
                        viewModel.editWallet(id, newAmount.toLong())
                    }
                }
            },
            deleteWallet = { id ->
                viewModel.deleteWallet(id)
            }
        )

        binding.rvWallet.adapter = adapter
        binding.rvWallet.layoutManager = LinearLayoutManager(requireContext())

        binding.ibAddWallet.setOnClickListener {
            Intent(requireContext(), NewWalletActivity::class.java).also {
                startActivity(it)
            }
        }

        observe()
    }

    private fun observe() {
        viewModel.wallets.observe(viewLifecycleOwner) {
            binding.pbWallet.visibility = View.GONE
            adapter.setWallet(it)
        }

        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        viewModel.totalAmount.observe(viewLifecycleOwner) {
            binding.tvTotalWallet.text = it
        }

        viewModel.isEdited.observe(viewLifecycleOwner) {
            if (it) {
                binding.clEditWallet.visibility = View.GONE

                Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isDeleted.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        if (getId != null && getOldAmount != null) {
            when (item.itemId) {
                701200 -> {
                    binding.clEditWallet.visibility = View.VISIBLE
                    binding.etAmount.setText(getOldAmount.toString())

                    binding.ibSave.setOnClickListener {
                        val newAmount = binding.etAmount.text.toString().trim()

                        if (newAmount.isNotBlank()) {
                            viewModel.editWallet(getId.toString(), newAmount.toLong())
                        }
                    }
                }
                701201 -> {
                    viewModel.deleteWallet(getId.toString())
                }
            }
        }

        return super.onContextItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

