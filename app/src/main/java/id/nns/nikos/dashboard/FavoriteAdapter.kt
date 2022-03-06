package id.nns.nikos.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.nns.nikos.R
import id.nns.nikos.databinding.ItemFavoriteBinding

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.ViewPagerViewHolder>() {

    private lateinit var binding: ItemFavoriteBinding
    private var images = mutableListOf<String>()

    fun setImages(images: ArrayList<String>) {
        this.images = images.toMutableList()
    }

    inner class ViewPagerViewHolder(private val vhBinding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(vhBinding.root) {

        fun bind(image: String) {
            Glide.with(itemView.context)
                .load(image)
                .placeholder(R.drawable.loading_animation)
                .into(vhBinding.ivDashboard)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteAdapter.ViewPagerViewHolder {
        binding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewPagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteAdapter.ViewPagerViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int {
        return images.size
    }

}
