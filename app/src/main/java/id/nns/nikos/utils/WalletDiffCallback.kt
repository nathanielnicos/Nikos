package id.nns.nikos.utils

import androidx.recyclerview.widget.DiffUtil
import id.nns.nikos.data.Wallet

class WalletDiffCallback(
    private val oldList: ArrayList<Wallet>,
    private val newList: ArrayList<Wallet>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].amount == newList[newItemPosition].amount &&
                oldList[oldItemPosition].logoUrl == newList[newItemPosition].logoUrl &&
                oldList[oldItemPosition].timestamp == newList[newItemPosition].timestamp
    }

}
