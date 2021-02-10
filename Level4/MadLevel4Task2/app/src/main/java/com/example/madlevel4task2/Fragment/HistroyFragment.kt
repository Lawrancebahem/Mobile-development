package com.example.madlevel4task2.Fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madlevel4task2.Adapter.HistoryAdapter
import com.example.madlevel4task2.DAO.HistoryRepository
import com.example.madlevel4task2.Model.History
import com.example.madlevel4task2.R
import com.example.madlevel4task2.databinding.FragmentHistoryBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class HistroyFragment : Fragment(), MenuItem.OnMenuItemClickListener {

    private var _binding: FragmentHistoryBinding? = null
    private val historyList = arrayListOf<History>()
    private val historyAdapter = HistoryAdapter(historyList)
    private lateinit var historyRepository: HistoryRepository

    private lateinit var me: Menu

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        historyRepository = HistoryRepository(requireContext())
        getHistoryFromDatabase()
        initViews()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_main, menu)
        menu.findItem(R.id.history).isVisible = false
        menu.findItem(R.id.delete).isVisible = true
        menu.findItem(R.id.delete).setOnMenuItemClickListener(this)
    }


    private fun initViews() {
        binding.rcView.layoutManager = LinearLayoutManager(activity)
        binding.rcView.adapter = historyAdapter
        onSwipe().attachToRecyclerView(binding.rcView)
    }


    /**
     * get the history from the database
     */
    private fun getHistoryFromDatabase() {
        historyList.clear()
        CoroutineScope(Dispatchers.Main).launch {
            val historyData = with(Dispatchers.IO) {
                historyRepository.getHistory()
            }
            historyList.addAll(historyData)
            historyAdapter.notifyDataSetChanged()
        }
    }

    /**
     * To delete allHistories
     */
    private fun onDelete() {

        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                historyRepository.deleteAllHistories()
            }
            getHistoryFromDatabase()
            historyAdapter.notifyDataSetChanged()
        }
    }

    /**
     * onSwipe to delete a certain result
     */

    private fun onSwipe(): ItemTouchHelper {

        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return true
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.IO){
                        historyRepository.deleteHistory(historyList[position])
                    }
                    getHistoryFromDatabase()
                }
            }
        }
        return ItemTouchHelper(callback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Once the delete item is clicked, this method gets fired
     */
    override fun onMenuItemClick(item: MenuItem?): Boolean {
        onDelete()
        return true
    }
}