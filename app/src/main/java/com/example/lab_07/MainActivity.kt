package com.example.lab_07

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.room.Room
import com.example.lab_07.ui.theme.Lab_07Theme
import com.example.mydatabase.ScreenMisTareas
import com.example.mydatabase.ScreenUser
import com.example.mydatabase.UserDatabase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab_07Theme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainContainer()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContainer() {
    var selectedScreen by remember { mutableStateOf(0) }

    val context = LocalContext.current
    val db = remember {
        Room.databaseBuilder(
            context,
            UserDatabase::class.java, "user_db"
        ).fallbackToDestructiveMigration().build()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(if (selectedScreen == 0) "Navegación Principal" else "Navegación Principal")
                },
                actions = {
                    IconButton(onClick = { selectedScreen = 0 }) {
                        Icon(Icons.Filled.Person, contentDescription = "Usuarios")
                    }
                    IconButton(onClick = { selectedScreen = 1 }) {
                        Icon(Icons.Filled.List, contentDescription = "Tareas")
                    }
                }
            )
        }
    ) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            when (selectedScreen) {
                0 -> ScreenUser()
                1 -> ScreenMisTareas(db.taskDao())
            }
        }
    }
}