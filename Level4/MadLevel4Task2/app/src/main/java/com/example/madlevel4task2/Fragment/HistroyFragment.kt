package com.example.madlevel4task2.Fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
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
class HistroyFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private var historyList = arrayListOf<History>()
    private val historyAdapter = HistoryAdapter(historyList)
    private lateinit var historyRepository: HistoryRepository

    private lateinit var me: Menu

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        historyRepository = HistoryRepository(requireContext())
        getHistoryFromDatabase()
        initViews()
    }

    private fun initViews() {
        binding.rcView.layoutManager = LinearLayoutManager(activity)
        binding.rcView.adapter = historyAdapter
    }


    /**
     * get the history from the database
     */
    fun getHistoryFromDatabase() {
        historyList.clear()
        CoroutineScope(Dispatchers.Main).launch {
            val historyData = with(Dispatchers.IO) {
                historyRepository.getHistory()
            }
            historyList.addAll(historyData)
            historyAdapter.notifyDataSetChanged()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("Has been clicked", "TRUE")
        return (when (item.itemId) {
            R.id.delete -> {
                onDelete()
                return true
            }
            else ->
                super.onOptionsItemSelected(item)
        })
    }

    fun onDelete() {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                historyRepository.deleteAllHistories()
            }
            historyAdapter.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}