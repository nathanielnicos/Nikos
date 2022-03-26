package id.nns.nikos.utils

import android.content.Context
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.IndexOutOfBoundsException

class WrapContentLinearLayoutManager(private val context: Context, orientation: Int, reverse: Boolean) :
    LinearLayoutManager(context, orientation, reverse) {

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: IndexOutOfBoundsException) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

}
