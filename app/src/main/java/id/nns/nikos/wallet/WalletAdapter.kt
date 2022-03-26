package id.nns.nikos.wallet

import android.view.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.nns.nikos.data.Wallet
import id.nns.nikos.databinding.ItemWalletBinding
import id.nns.nikos.utils.FormattedDateTime.convertDate
import id.nns.nikos.utils.FormattedPrice.getPrice
import id.nns.nikos.utils.WalletDiffCallback

class WalletAdapter(
    private val keyWallet: String,
    private val getData: (String, Long) -> Unit,
    private val editWallet: (String, Long) -> Unit,
    private val deleteWallet: (String) -> Unit
) : RecyclerView.Adapter<WalletAdapter.WalletViewHolder>() {

    private lateinit var binding: ItemWalletBinding
    private var wallets = ArrayList<Wallet>()

    fun setWallet(newWallets: ArrayList<Wallet>) {
        val diffCallback = WalletDiffCallback(wallets, newWallets)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        wallets.clear()
        wallets.addAll(newWallets)

        diffResult.dispatchUpdatesTo(this)
    }

    inner class WalletViewHolder(private val vhBinding: ItemWalletBinding) :
        RecyclerView.ViewHolder(vhBinding.root),
        View.OnCreateContextMenuListener {

        fun bind(wallet: Wallet) {
            Glide.with(itemView.context)
                .load(wallet.logoUrl)
                .into(vhBinding.ivType)

            binding.tvAmount.text = getPrice(wallet.amount ?: 0)
            binding.tvTimestamp.text = wallet.timestamp?.let { convertDate(it) }

            when (keyWallet) {
                "long_press" -> {
                    onLongPress(wallet)
                }
                "icon_button" -> {
                    onClickIconButton(wallet)
                }
                "both" -> {
                    onLongPress(wallet)
                    onClickIconButton(wallet)
                }
            }
        }

        private fun onLongPress(wallet: Wallet) {
            binding.clItemWallet.setOnCreateContextMenuListener(this)
            binding.clItemWallet.setOnLongClickListener {
                wallet.amount?.let { amount -> getData(wallet.id.toString(), amount) }
                false
            }
        }

        private fun onClickIconButton(wallet: Wallet) {
            binding.ibEdit.visibility = View.VISIBLE
            binding.ibDelete.visibility = View.VISIBLE

            binding.ibEdit.setOnClickListener {
                wallet.amount?.let { amount -> editWallet(wallet.id.toString(), amount) }
            }

            binding.ibDelete.setOnClickListener {
                deleteWallet(wallet.id.toString())
            }
        }

        override fun onCreateContextMenu(
            p0: ContextMenu?,
            p1: View?,
            p2: ContextMenu.ContextMenuInfo?
        ) {
            p0?.add(this.adapterPosition, 701200, 0, "Edit")
            p0?.add(this.adapterPosition, 701201, 1, "Delete")
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WalletAdapter.WalletViewHolder {
        binding = ItemWalletBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WalletViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WalletAdapter.WalletViewHolder, position: Int) {
        holder.bind(wallets[position])
    }

    override fun getItemCount(): Int {
        return wallets.size
    }

}
