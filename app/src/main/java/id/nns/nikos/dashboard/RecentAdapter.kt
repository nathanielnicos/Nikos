package id.nns.nikos.dashboard

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.nns.nikos.R
import id.nns.nikos.data.Product
import id.nns.nikos.databinding.ItemRecentBinding
import id.nns.nikos.detail.DetailActivity
import id.nns.nikos.utils.FavoriteState.favoriteState
import id.nns.nikos.utils.FormattedPrice.getPrice
import id.nns.nikos.utils.PayDiffCallback

class RecentAdapter(
    private val action: (id: String, isFavorite: Boolean) -> Unit
) : RecyclerView.Adapter<RecentAdapter.RecentViewHolder>() {

    private var oldProductList = ArrayList<Product>()
    private lateinit var binding: ItemRecentBinding

    fun setData(newProductList: ArrayList<Product>) {
        val payDiffCallback = PayDiffCallback(oldProductList, newProductList)
        val diffResult = DiffUtil.calculateDiff(payDiffCallback)

        oldProductList.clear()
        oldProductList.addAll(newProductList)

        diffResult.dispatchUpdatesTo(this)
    }

    inner class RecentViewHolder(private val vhBinding: ItemRecentBinding) :
        RecyclerView.ViewHolder(vhBinding.root) {

        fun bind(product: Product) {
            Glide.with(itemView.context)
                .load(product.imgUrl)
                .placeholder(R.drawable.loading_animation)
                .into(vhBinding.ivProduct)

            binding.tvProductName.text = product.product
            binding.tvPrice.text = getPrice(product.price ?: 0)
            binding.ibFavorite.setBackgroundResource(
                favoriteState(product.favorite ?: false)
            )

            binding.ibFavorite.setOnClickListener {
                action(product.id.toString(), product.favorite?.not() ?: false)
            }

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.KEY_DETAIL, product.id)
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
        holder.bind(oldProductList[position])
    }

    override fun getItemCount(): Int {
        return oldProductList.size
    }

}
