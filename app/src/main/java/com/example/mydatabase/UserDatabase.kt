package com.example.mydatabase

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [User::class, Task::class, Routine::class], version = 3)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun taskDao(): TaskDao
    abstract fun routineDao(): RoutineDao
}