package id.nns.nikos.new_wallet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.nns.nikos.data.Icon
import id.nns.nikos.databinding.ItemWalletIconBinding
import id.nns.nikos.utils.IconDiffCallback

class NewWalletAdapter(
    private val onClickIcon: (String?, String?) -> Unit
) : RecyclerView.Adapter<NewWalletAdapter.AddWalletViewHolder>() {

    private lateinit var binding: ItemWalletIconBinding
    private val icons = ArrayList<Icon>()

    fun setIcon(newIcons: ArrayList<Icon>) {
        val diffCallback = IconDiffCallback(icons, newIcons)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        icons.clear()
        icons.addAll(newIcons)

        diffResult.dispatchUpdatesTo(this)
    }

    inner class AddWalletViewHolder(private val vhBinding: ItemWalletIconBinding) :
        RecyclerView.ViewHolder(vhBinding.root) {

            fun bind(icon: Icon) {
                Glide.with(itemView.context)
                    .load(icon.iconUrl)
                    .into(vhBinding.ibWalletIcon)

                vhBinding.tvWalletIcon.text = icon.name

                itemView.setOnClickListener {
                    onClickIcon(icon.name, icon.iconUrl)
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddWalletViewHolder {
        binding = ItemWalletIconBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddWalletViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddWalletViewHolder, position: Int) {
        holder.bind(icons[position])
    }

    override fun getItemCount(): Int {
        return icons.size
    }

}
