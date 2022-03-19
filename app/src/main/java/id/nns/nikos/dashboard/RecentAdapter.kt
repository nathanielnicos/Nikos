package id.nns.nikos.dashboard

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.nns.nikos.R
import id.nns.nikos.data.Pay
import id.nns.nikos.databinding.ItemRecentBinding
import id.nns.nikos.detail.DetailActivity
import id.nns.nikos.utils.FavoriteState.favoriteState
import id.nns.nikos.utils.FormattedPrice.getPrice
import id.nns.nikos.utils.PayDiffCallback

class RecentAdapter(
    private val action: (id: String, isFavorite: Boolean) -> Unit
) : RecyclerView.Adapter<RecentAdapter.RecentViewHolder>() {

    private var oldPayList = ArrayList<Pay>()
    private lateinit var binding: ItemRecentBinding

    fun setData(newPayList: ArrayList<Pay>) {
        val payDiffCallback = PayDiffCallback(oldPayList, newPayList)
        val diffResult = DiffUtil.calculateDiff(payDiffCallback)

        oldPayList.clear()
        oldPayList.addAll(newPayList)

        diffResult.dispatchUpdatesTo(this)
    }

    inner class RecentViewHolder(private val vhBinding: ItemRecentBinding) :
        RecyclerView.ViewHolder(vhBinding.root) {

        fun bind(pay: Pay) {
            Glide.with(itemView.context)
                .load(pay.imgUrl)
                .placeholder(R.drawable.loading_animation)
                .into(vhBinding.ivProduct)

            binding.tvProductName.text = pay.product
            binding.tvPrice.text = getPrice(pay.price)
            binding.ibFavorite.setBackgroundResource(
                favoriteState(pay.favorite)
            )

            binding.ibFavorite.setOnClickListener {
                action(pay.id, !pay.favorite)
            }

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.KEY_DETAIL, pay)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecentAdapter.RecentViewHolder {
        binding = ItemRecentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecentAdapter.RecentViewHolder, position: Int) {
        holder.bind(oldPayList[position])
    }

    override fun getItemCount(): Int {
        return oldPayList.size
    }

}
