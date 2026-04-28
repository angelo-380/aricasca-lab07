package com.example.mydatabase

import androidx.room.Database
import androidx.room.RoomDatabase

// Subimos la versión a 2 porque estamos agregando una tabla nueva (Task)
@Database(entities = [User::class, Task::class], version = 2)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    // Agregamos el acceso para las tareas
    abstract fun taskDao(): TaskDao
}