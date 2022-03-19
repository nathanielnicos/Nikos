package id.nns.nikos.utils

import androidx.recyclerview.widget.DiffUtil
import id.nns.nikos.data.Pay

class PayDiffCallback(
    private val oldList: ArrayList<Pay>,
    private val newList: ArrayList<Pay>
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
        return oldList[oldItemPosition].detail == newList[newItemPosition].detail &&
                oldList[oldItemPosition].favorite == newList[newItemPosition].favorite &&
                oldList[oldItemPosition].imgUrl == newList[newItemPosition].imgUrl &&
                oldList[oldItemPosition].price == newList[newItemPosition].price &&
                oldList[oldItemPosition].product == newList[newItemPosition].product &&
                oldList[oldItemPosition].timestamp == newList[newItemPosition].timestamp
    }

}
