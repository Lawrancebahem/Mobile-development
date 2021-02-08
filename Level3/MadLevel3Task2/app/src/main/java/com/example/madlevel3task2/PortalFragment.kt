package com.example.madlevel3task2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.example.madlevel3task2.databinding.FragmentPortalBinding
import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class PortalFragment : Fragment() {

    private var _binding: FragmentPortalBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentPortalBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addProtalBtn.setOnClickListener {
            onAddPortal()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * To add the added portal to the bundle, the start fragment is listening to the result that is sent by this method
     */
    fun onAddPortal() {

        val title = binding.titlePortal.text.toString()
        val url = binding.URLPortal.text.toString()
        val isCorrect =  android.util.Patterns.WEB_URL.matcher(url).matches()
        if (!isCorrect) {
            Snackbar.make(binding.fragmentLayout, "Please provide a valid URL", Snackbar.LENGTH_SHORT).show()
            return
        }
        val newPortal = Portal(title, url)
        setFragmentResult(KEY, bundleOf(Pair(BUNDLE_KEY, newPortal)))
        //"pop" the backstack, this means we destroy
        //this fragment and go back to the RemindersFragment
        findNavController().popBackStack()
    }

}