package com.example.shop.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shop.R
import com.example.shop.adapter.ImageAdapter
import com.example.shop.adapter.SpinnerAdapter
import com.example.shop.databinding.FragmentAddItemBinding
import com.example.shop.viewModel.AdvertisementViewModel
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import com.example.shop.model.Category


/**
 * A simple [Fragment] subclass.
 * Use the [AddItemFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddItemFragment : Fragment() {

    private lateinit var binding: FragmentAddItemBinding
    private lateinit var imageAdapter: ImageAdapter
    private val advertisementViewModel: AdvertisementViewModel by activityViewModels()
    private lateinit var builder:AlertDialog.Builder


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddItemBinding.inflate(inflater, container, false)

        val itemsSpinner = ArrayList<Category>()
        for (i in Category.values()){
            itemsSpinner.add(i)
        }

        val adapterSpinner = SpinnerAdapter(requireContext(), android.R.layout.simple_spinner_item, itemsSpinner)
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categoryDrop.adapter = adapterSpinner


        binding.categoryDrop.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
//                if (itemsSpinner[0]getString(R.string.choose)){
//                    itemsSpinner.removeAt(0)
//                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }



    @SuppressLint("ResourceAsColor")
    private fun init(){
        builder =  AlertDialog.Builder(requireContext())

        //change the home indicator
        val closeIcon = requireContext().resources.getDrawable(R.drawable.clear)
        closeIcon.setTint(R.color.black)
        val actionBar: ActionBar? = (activity as AppCompatActivity?)!!.supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(closeIcon)
        actionBar?.title = getString(R.string.adOverview)
        setHasOptionsMenu(true)

        //set the adapter
        imageAdapter = ImageAdapter(advertisementViewModel.bitmapList.value!!, ::onDelete)

        //adapter layout
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.imageContainer.layoutManager = layoutManager
        binding.imageContainer.adapter = imageAdapter
        imageAdapter.notifyDataSetChanged()


        binding.addMore.setOnClickListener{
            findNavController().navigate(R.id.cameraFragment)
        }

    }
    private fun onAddMore() {

    }

    /**
     * When the user wants to leave
     */
    private fun onClose() {
        val alertBuilder = getDialog(getString(R.string.sureClose))
        alertBuilder.setPositiveButton(getString(R.string.yes)) { dialog, id ->
            // Delete selected note from database
            advertisementViewModel.bitmapList.value!!.clear()
            imageAdapter.notifyDataSetChanged()
            findNavController().navigate(R.id.homeFragment)
        }
        alertBuilder.create()
        alertBuilder.show()
    }


    /**
     * When clicking on item to delete
     */
    private fun onDelete(index: Int){
        val alertBuilder = getDialog(getString(R.string.sureDelete))
        alertBuilder.setPositiveButton(getString(R.string.yes)) { dialog, id ->
            // Delete selected note from database
            advertisementViewModel.bitmapList.value!!.removeAt(index)
            imageAdapter.notifyDataSetChanged()
        }
        alertBuilder.create()
        alertBuilder.show()

    }

    /**
     * To create dialog builder and return for further additions
     */
    private fun getDialog(message: String): AlertDialog.Builder {
        builder.setMessage(message)
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { dialog, id ->
                // Dismiss the dialog
                dialog.dismiss()
            }
        return builder
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onClose()
        return true
    }
}