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

        setViewModel()
        setRecentAdapter()
        onClickView()
        observe()
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(this)[DashboardViewModel::class.java]
        viewModel.getImages(
            settingsPref.getPostNumber()
        )
        viewModel.getPays(
            settingsPref.getPostNumber()
        )
    }

    private fun setRecentAdapter() {
        recentAdapter = RecentAdapter { id, isFavorite ->
            viewModel.updateFavoriteState(id, isFavorite)
        }
        binding.rvRecent.layoutManager = LinearLayoutManager(activity)
        binding.rvRecent.adapter = recentAdapter
    }

    private fun onClickView() {
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
            Toast.makeText(
                activity,
                "Change the number of dashboard post at settings!",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.tvSeeMoreBottom.setOnClickListener {
            Toast.makeText(
                activity,
                "Change the number of dashboard post at settings!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun observe() {
        viewModel.favoriteProduct.observe(viewLifecycleOwner) { products ->
            favoriteAdapter = FavoriteAdapter(products)

            binding.rvFavorite.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            binding.rvFavorite.adapter = favoriteAdapter
        }

        viewModel.recentProduct.observe(viewLifecycleOwner) { products ->
            binding.pbRecent.visibility = View.GONE
            recentAdapter.setData(products)
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
