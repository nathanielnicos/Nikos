package id.nns.nikos.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import id.nns.nikos.data.Pay
import id.nns.nikos.databinding.ActivityDetailBinding
import id.nns.nikos.utils.FormattedDateTime.convertDate
import id.nns.nikos.utils.FormattedDateTime.convertTime
import id.nns.nikos.utils.FormattedPrice.getPrice

class DetailActivity : AppCompatActivity() {

    companion object {
        const val KEY_DETAIL = "key_detail"
    }

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        val pay = intent.getParcelableExtra<Pay>(KEY_DETAIL)

        if (pay != null) {
            Glide.with(this)
                .load(pay.imgUrl)
                .into(binding.ivImageDetail)

            binding.tvNameDetail.text = pay.product
            binding.tvDetailDetail.text = pay.detail
            binding.tvDateDetail.text = convertDate(pay.timestamp)
            binding.tvTimeDetail.text = convertTime(pay.timestamp)
            binding.tvPriceDetail.text = getPrice(pay.price)

            binding.ibEditDetail.setOnClickListener {
                binding.tvNameDetail.visibility = View.INVISIBLE
                binding.tvDetailDetail.visibility = View.INVISIBLE
                binding.ibEditDetail.visibility = View.INVISIBLE
                binding.etEditProduct.visibility = View.VISIBLE
                binding.etEditDetail.visibility = View.VISIBLE
                binding.ibSaveDetail.visibility = View.VISIBLE

                binding.etEditProduct.setText(pay.product)
                binding.etEditDetail.setText(pay.detail)
            }

            binding.ibSaveDetail.setOnClickListener {
                val newName = binding.etEditProduct.text.toString().trim()
                val newDetail = binding.etEditDetail.text.toString().trim()

                if (newName.isNotBlank() && newDetail.isNotBlank()) {
                    viewModel.savePay(pay.id, newName, newDetail)
                }
            }
        }

        observe()
    }

    private fun observe() {
        viewModel.isSuccess.observe(this) {
            if (it) {
                binding.tvNameDetail.text = binding.etEditProduct.text
                binding.tvDetailDetail.text = binding.etEditDetail.text

                binding.tvNameDetail.visibility = View.VISIBLE
                binding.tvDetailDetail.visibility = View.VISIBLE
                binding.ibEditDetail.visibility = View.VISIBLE
                binding.etEditProduct.visibility = View.INVISIBLE
                binding.etEditDetail.visibility = View.INVISIBLE
                binding.ibSaveDetail.visibility = View.INVISIBLE

                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.error.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

}
