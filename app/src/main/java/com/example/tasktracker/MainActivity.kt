package com.example.tasktracker

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.tasktracker.data.Task
import com.example.tasktracker.data.TaskDao
import com.example.tasktracker.data.TaskTrackerDB
import com.example.tasktracker.databinding.ActivityMainBinding
import com.example.tasktracker.databinding.DialogAddTaskBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: TaskTrackerDB
    private val taskDao: TaskDao by lazy { database.getTaskDao() }
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
         database =  TaskTrackerDB.createDatabase(this)





    }

    private fun showAddDialog() {
        val dialogBinding = DialogAddTaskBinding.inflate(layoutInflater)
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(dialogBinding.root)
        dialogBinding.buttonShowDetails.setOnClickListener {
          dialogBinding.editTextTaskDesc.visibility =
              if (dialogBinding.editTextTaskDesc.visibility == View.VISIBLE)  View.GONE else View.VISIBLE
        }

        dialogBinding.buttonSave.setOnClickListener {
            val task = Task(
                title = dialogBinding.editTextTaskTitle.text.toString(),
                description = dialogBinding.editTextTaskDesc.text.toString()
            )
            thread {
                taskDao.createTask(task)
            }
            dialog.dismiss()
        }
        dialog.show()
    }

    inner class PagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(this) {
        override fun getItemCount(): Int  = 1
        override fun createFragment(position: Int): Fragment {
            return TasksFragment()
        }
    }
}