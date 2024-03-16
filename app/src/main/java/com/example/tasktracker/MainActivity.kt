package com.example.tasktracker

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.tasktracker.data.Task
import com.example.tasktracker.data.TaskTrackerDB
import com.example.tasktracker.databinding.ActivityMainBinding
import com.example.tasktracker.databinding.DialogAddTaskBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.pager.adapter = PagerAdapter(this)

        TabLayoutMediator(binding.tabs, binding.pager) { tab, position ->
            tab.text = "Tasks"
        }.attach()

        binding.fab.setOnClickListener {
            showAddDialog()
        }
        val database =  TaskTrackerDB.createDatabase(this)

        val taskDao =  database.getTaskDao()

        thread{
            taskDao.createTask(Task(title = "A third task"))
           val tasks =  taskDao.getAllTasks()
            runOnUiThread {
                Toast.makeText(this, "Number of tasks: ${tasks.size}", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun showAddDialog() {
        val dialogBinding = DialogAddTaskBinding.inflate(layoutInflater)
        MaterialAlertDialogBuilder(this)
            .setTitle("Add a new task")
            .setView(dialogBinding.root)
            .setPositiveButton("Save") { _, _ ->
                Toast.makeText(
                    this,
                    "Your task is: ${dialogBinding.editText.text}",
                    Toast.LENGTH_SHORT
                ).show()
                // Respond to positive button press
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    inner class PagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(this) {
        override fun getItemCount(): Int  = 1
        override fun createFragment(position: Int): Fragment {
            return TasksFragment()
        }
    }
}