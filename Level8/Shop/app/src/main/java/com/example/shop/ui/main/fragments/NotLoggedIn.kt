package com.example.shop.ui.main.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.shop.R
import com.example.shop.databinding.FragmentNotLoggedInBinding
import com.example.shop.ui.main.viewModel.NotLoggedInViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [NotLoggedIn.newInstance] factory method to
 * create an instance of this fragment.
 */
class NotLoggedIn : Fragment() {

    private lateinit var binding: FragmentNotLoggedInBinding

    private val notLoggedInViewModel: NotLoggedInViewModel by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotLoggedInBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("LongLogTag")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.logBtn.setOnClickListener {
            findNavController().navigate(R.id.action_notLoggedIn_to_loginFragment)
        }

        displayInfo()

    }

    /**
     * To show the appropriate info about the selected page, if the user is not logged ni
     */
    private fun displayInfo() {

        notLoggedInViewModel.info.observe(viewLifecycleOwner) { info ->
            notLoggedInViewModel.imageName.observe(viewLifecycleOwner) { imageName ->
                binding.info.text = info
                binding.imageView2.setImageResource(resources.getIdentifier(imageName, "drawable", context?.packageName))
            }
        }

    }
}
