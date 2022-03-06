package id.nns.nikos.new_post

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.canhub.cropper.*
import id.nns.nikos.databinding.ActivityPostBinding

class PostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostBinding
    private lateinit var viewModel: PostViewModel
    private var imgUri: Uri? = null

    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // use the returned uri
            imgUri = result.uriContent

            Glide.with(this)
                .load(imgUri)
                .into(binding.ivImagePost)

            binding.ibAddPhoto.visibility = View.INVISIBLE
        } else {
            // an error occurred
            Toast.makeText(this, result.error?.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[PostViewModel::class.java]

        binding.ibAddPhoto.setOnClickListener {
            startCrop()
        }

        binding.ibSavePay.setOnClickListener {
            binding.pbPost.visibility = View.VISIBLE
            binding.ibSavePay.visibility = View.INVISIBLE

            val product = binding.etProductPost.text.toString().trim()
            val details = binding.etDetailPost.text.toString().trim()
            val price = binding.etAmountPost.text.toString().trim()

            if (product.isNotBlank() && details.isNotBlank() && price.isNotBlank() && imgUri != null) {
                viewModel.savePost(
                    uri = imgUri as Uri,
                    product = product,
                    details = details,
                    price = price.toLong()
                )
            }
        }

        observe()
    }

    private fun observe() {
        viewModel.isSuccess.observe(this) {
            if (it) {
                finish()
            } else {
                binding.pbPost.visibility = View.GONE
                binding.ibSavePay.visibility = View.VISIBLE
            }
        }

        viewModel.error.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            binding.pbPost.visibility = View.GONE
            binding.ibSavePay.visibility = View.VISIBLE
        }
    }

    private fun startCrop() {
        cropImage.launch(
            options {
                setGuidelines(CropImageView.Guidelines.ON)
            }
        )
    }

}
