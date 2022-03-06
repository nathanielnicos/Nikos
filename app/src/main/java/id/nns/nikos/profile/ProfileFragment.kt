package id.nns.nikos.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import id.nns.nikos.R
import id.nns.nikos.databinding.FragmentProfileBinding
import id.nns.nikos.main.MainActivity
import id.nns.nikos.utils.SettingPreference

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding get() = _binding as FragmentProfileBinding

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        viewModel.getProfile()

        binding.tvMap.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_mapFragment)
        }

        binding.tvSetting.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_settingsFragment)
        }

        binding.btnLogout.setOnClickListener {
            viewModel.signOut()
        }

        showSettingPreference()

        observe()
    }

    private fun showSettingPreference() {
        val settingPref = context?.let { SettingPreference(it) }

        if (settingPref != null) {
            val mapState = settingPref.getMapState()

            if (!mapState) {
                binding.tvMap.visibility = View.INVISIBLE
                binding.dividerOne.visibility = View.INVISIBLE
            }
        }
    }

    private fun observe() {
        viewModel.isSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                val intent = Intent(requireContext(), MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or
                        Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                activity?.finish()
            }
        }

        viewModel.profile.observe(viewLifecycleOwner) { profile ->
            if (profile != null) {
                Glide.with(requireContext())
                    .load(profile.imgUri)
                    .placeholder(R.drawable.logo_small)
                    .into(binding.civProfile)
                binding.tvName.text = profile.name ?: "No Name"
                binding.tvEmail.text = profile.email ?: "No Email"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
