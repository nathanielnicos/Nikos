package id.nns.nikos.utils

import androidx.recyclerview.widget.DiffUtil
import id.nns.nikos.data.Icon

class IconDiffCallback(
    private val oldList: ArrayList<Icon>,
    private val newList: ArrayList<Icon>
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
        return oldList[oldItemPosition].name == newList[newItemPosition].name &&
                oldList[oldItemPosition].iconUrl == newList[newItemPosition].iconUrl
    }

}
