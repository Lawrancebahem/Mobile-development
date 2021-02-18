package com.example.madlevel7task1

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.madlevel7task1.databinding.FragmentProfileBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ProfileFragment : Fragment() {


    private val viewModel: ProfileViewModel by activityViewModels()

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root

    }

    private fun observeProfile() {
        viewModel.getProfile()

        viewModel.profile.observe(viewLifecycleOwner, Observer {
            val profile = it

            binding.tvName.text = getString(R.string.profile_name, profile.firstName, profile.lastName)
            binding.tvDescription.text = profile.description
            if (profile.imageUri!!.isNotEmpty()) {
                binding.ivProfileImage.setImageURI(Uri.parse(profile.imageUri))
            }
        })
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeProfile()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}