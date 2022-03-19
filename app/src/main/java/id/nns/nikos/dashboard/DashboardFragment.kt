package id.nns.nikos.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.nns.nikos.databinding.FragmentDashboardBinding
import id.nns.nikos.new_post.PostActivity
import id.nns.nikos.profile.ProfileActivity
import id.nns.nikos.utils.SettingPreference

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding as FragmentDashboardBinding
    private lateinit var viewModel: DashboardViewModel
    private lateinit var favoriteAdapter: FavoriteAdapter
    private lateinit var recentAdapter: RecentAdapter
    private lateinit var settingsPref: SettingPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingsPref = SettingPreference(requireContext())
        viewModel = ViewModelProvider(this)[DashboardViewModel::class.java]
        viewModel.getPays(
            settingsPref.getPostNumber()
        )

        // Favorite Adapter
        favoriteAdapter = FavoriteAdapter()
        favoriteAdapter.setImages(
            arrayListOf(
                "https://firebasestorage.googleapis.com/v0/b/nikos-f05f9.appspot.com/o/IMG_20220129_195025.jpg?alt=media&token=e027b37e-2342-4282-a4b6-7fac7f347ff1",
                "https://firebasestorage.googleapis.com/v0/b/nikos-f05f9.appspot.com/o/IMG20220115095407.jpg?alt=media&token=26c22a29-b20f-4ba5-9622-89e196ede9e9",
                "https://firebasestorage.googleapis.com/v0/b/nikos-f05f9.appspot.com/o/IMG20220128120915.jpg?alt=media&token=2583230a-4ec7-47bd-9ee0-ed32d04102ab",
                "https://firebasestorage.googleapis.com/v0/b/nikos-f05f9.appspot.com/o/IMG_20220129_194610.jpg?alt=media&token=8c0cbed0-c599-4671-ade3-cba9d9a4a649",
                "https://firebasestorage.googleapis.com/v0/b/nikos-f05f9.appspot.com/o/IMG_20220129_194644.jpg?alt=media&token=8b4ba828-ad23-400b-877b-e446a342b41c"
            )
        )
        binding.viewPager.adapter = favoriteAdapter

        // Recent Adapter
        recentAdapter = RecentAdapter { id, isFavorite ->
            viewModel.updateFavoriteState(id, isFavorite)
        }

        binding.rvRecent.adapter = recentAdapter
        binding.rvRecent.layoutManager = LinearLayoutManager(activity)

        binding.circleImageView.setOnClickListener {
            Intent(activity, ProfileActivity::class.java).also {
                startActivity(it)
            }
        }
        
        binding.ibAddPay.setOnClickListener {
            Intent(activity, PostActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.tvSeeMoreTop.setOnClickListener {
            Toast.makeText(activity, "Change the number of dashboard post at settings!", Toast.LENGTH_SHORT).show()
        }

        binding.tvSeeMoreBottom.setOnClickListener {
            Toast.makeText(activity, "Change the number of dashboard post at settings!", Toast.LENGTH_SHORT).show()
        }

        observe()
    }

    private fun observe() {
        viewModel.pays.observe(viewLifecycleOwner) { pays ->
            binding.pbRecent.visibility = View.GONE
            recentAdapter.setData(pays)
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            binding.pbRecent.visibility = View.GONE
            Toast.makeText(activity, error, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
