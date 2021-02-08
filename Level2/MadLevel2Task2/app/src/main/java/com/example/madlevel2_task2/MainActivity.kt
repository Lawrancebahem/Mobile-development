package com.example.madlevel2_task2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madlevel2_task2.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), QuizAdapter.OnItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private val quizzes = arrayListOf<Quiz>()
    private val quizAdapter = QuizAdapter(quizzes, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }


    private fun initView() {

        quizzes.add(Quiz("Mobile application development grants 12 ECTS", false))
        quizzes.add(Quiz("A unit in kotlin corresponds to a void in Java", true))
        quizzes.add(Quiz("A 'var' 'val' are the same", false))
        quizzes.add(Quiz("In Kotlin 'when' replaces the 'switch' operator in java", true))

        binding.rvQuizzes.layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
        binding.rvQuizzes.adapter = quizAdapter
        binding.rvQuizzes.addItemDecoration(DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL))

        createItemTouchHelper().attachToRecyclerView(binding.rvQuizzes)

    }

    /**
     * Create a touch helper to recognize when a user swipes an item from a recycler view.
     * An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
     * and uses callbacks to signal when a user is performing these actions.
     */
    private fun createItemTouchHelper(): ItemTouchHelper {

        // Callback which is used to create the ItemTouch helper. Only enables left swipe.
        // Use ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) to also enable right swipe.
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            // Enables or Disables the ability to move items up and down.
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            // Callback triggered when a user swiped an item.
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val rightCorrect = direction == ItemTouchHelper.RIGHT && quizzes[position].isCorrect
                val leftCorrect = direction == ItemTouchHelper.LEFT && !quizzes[position].isCorrect
                if (rightCorrect || leftCorrect) {
                    quizzes.removeAt(position)
                } else {
                    Snackbar.make(binding.rvQuizzes, "Wrong", Snackbar.LENGTH_SHORT).show()
                }
                quizAdapter.notifyDataSetChanged()
            }
        }
        return ItemTouchHelper(callback)
    }

    /**
     * Show a toast message about clicked quiz
     */
    override fun onItemClick(position: Int) {
        val clickedQuiz = quizzes[position]
        Toast.makeText(this, "The quiz: ${clickedQuiz.text} is : ${clickedQuiz.isCorrect}", Toast.LENGTH_LONG).show()
    }
}