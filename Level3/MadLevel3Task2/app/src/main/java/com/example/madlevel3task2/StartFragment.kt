package com.example.madlevel3task2

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.GridLayoutManager
import com.example.madlevel3task2.databinding.FragmentStartBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class StartFragment : Fragment(), OnItemClick {

    var portals = arrayListOf(
            Portal("VLO", "http://vlo.informatica.hva.nl"),
            Portal("Roosters", "http://roosters.hva.nl"),
            Portal("Sis", "http://sis.hva.nl"),
            Portal("HvA", "http://hva.nl"),
    )

    private var portalAdapter = PortalAdapter(portals, this)
    private var _binding: FragmentStartBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * apply the grid layout to the RecyclerView, and set its adapter to portal adapter
     */
    fun initView() {
        // make the layout as grid, and two items each row
        binding.revItem.layoutManager = GridLayoutManager(context, 2)
        binding.revItem.adapter = portalAdapter
        observeResultBundle()
    }

    /**
     * To extract the new portal from the given bundle
     */
    fun observeResultBundle() {
        setFragmentResultListener(KEY) { key, bundle ->
            val portal = bundle.get(BUNDLE_KEY) as Portal
            portals.add(portal)
            Log.d("PORTAL LIST SIZE: ", portals.size.toString())
            portalAdapter.notifyDataSetChanged()
        }
    }

    /**
     * To listen to a click
     */
    override fun onItemClick(index: Int) {
        var clickedPortal = portals[index]
        context?.let {context ->
            clickedPortal.url?.let { url ->
                openTabLink(url, context)
            }
        }
    }

    /**
     * Method to open a tab in the browser with the given url
     */
    private fun openTabLink(url: String, context: Context) {
        var customTabIntent = CustomTabsIntent.Builder().build()
        customTabIntent.launchUrl(context, Uri.parse(url))
    }
}