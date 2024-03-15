package com.example.tasktracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tasktracker.data.Task

@Database(entities = [Task::class], version =1)
abstract class TaskTrackerDB : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}

fun createDatabase(context: Context) : TaskTrackerDB {
        return  Room.databaseBuilder(
        context,
        TaskTrackerDB::class.java,
        "task_tracker_db"
    ).build()
}