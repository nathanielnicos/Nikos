package id.nns.nikos.dashboard

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.nns.nikos.R
import id.nns.nikos.data.Product
import id.nns.nikos.databinding.ItemFavoriteBinding
import id.nns.nikos.detail.DetailActivity

class FavoriteAdapter(private val products: ArrayList<Product>) :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    private lateinit var binding: ItemFavoriteBinding

    inner class FavoriteViewHolder(private val vhBinding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(vhBinding.root) {

        fun bind(product: Product) {
            Glide.with(itemView.context)
                .load(product.imgUrl)
                .placeholder(R.drawable.loading_animation)
                .into(vhBinding.ivDashboard)

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
    ): FavoriteAdapter.FavoriteViewHolder {
        binding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteAdapter.FavoriteViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int {
        return products.size
    }

}
