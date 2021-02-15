package com.example.madlevel5task2.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madlevel5task2.R
import com.example.madlevel5task2.adapter.GameAdapter
import com.example.madlevel5task2.databinding.FragmentGameBacklogBinding
import com.example.madlevel5task2.model.Game

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class GameBacklogFragment : Fragment() {

    private var _binding: FragmentGameBacklogBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val gamesList = ArrayList<Game>()

    private val gameAdapter:GameAdapter = GameAdapter(gamesList)

    private val gameViewModel:GameViewModel by viewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        _binding = FragmentGameBacklogBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rcView.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL, false)
        binding.rcView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        binding.rcView.adapter = gameAdapter
        observeNewGames()

        createItemTouchHelper().attachToRecyclerView(binding.rcView)
    }



    @SuppressLint("NotifyDataSetChanged")
    fun observeNewGames(){
        gameViewModel.games.observe(viewLifecycleOwner){ games ->
            gamesList.clear()
            gamesList.addAll(games)
            gameAdapter.notifyDataSetChanged()
        }
    }


    private fun createItemTouchHelper(): ItemTouchHelper {

        // Callback which is used to create the ItemTouch helper. Only enables left swipe.
        // Use ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) to also enable right swipe.
        // WTF? ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT literally??
        val callback =
            object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

                // Enables or Disables the ability to move items up and down.
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                // Callback triggered when a user swiped an item.
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    gameViewModel.deleteGame(gamesList[position])

                    gameViewModel.sucsess.observe(viewLifecycleOwner){
                        Toast.makeText(context,getString(R.string.success_delete), Toast.LENGTH_SHORT).show()
                    }

                    gameAdapter.notifyDataSetChanged()
                }
            }
        return ItemTouchHelper(callback)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}