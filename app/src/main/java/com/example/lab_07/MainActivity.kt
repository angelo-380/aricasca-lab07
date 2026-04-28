package com.example.lab_07

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.room.Room
import com.example.lab_07.ui.theme.Lab_07Theme
import com.example.mydatabase.ScreenMenuPrincipal
import com.example.mydatabase.ScreenMisTareas
import com.example.mydatabase.ScreenRutina
import com.example.mydatabase.ScreenDibujar
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
                    val titleText = when (selectedScreen) {
                        1 -> "Mis Tareas"
                        2 -> "Rutina"
                        3 -> "Dibujar"
                        4 -> "Gestión de Usuarios"
                        else -> "Menú Principal"
                    }
                    Text(titleText, fontWeight = FontWeight.Bold, color = Color.White)
                },
                navigationIcon = {
                    if (selectedScreen != 0) {
                        IconButton(onClick = { selectedScreen = 0 }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                        }
                    }
                },
                actions = {
                    // Botón para ir a la vista de Usuarios (siempre visible)
                    IconButton(onClick = { selectedScreen = 4 }) {
                        Icon(Icons.Filled.Person, contentDescription = "Usuarios", tint = Color.White)
                    }
                    if (selectedScreen == 0) {
                        IconButton(onClick = { }) {
                            Icon(Icons.Filled.Settings, contentDescription = "Configuración", tint = Color.White)
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFF27B50)
                )
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier.padding(innerPadding),
            color = Color(0xFFFAF0E6)
        ) {
            when (selectedScreen) {
                0 -> ScreenMenuPrincipal(onNavigate = { screen -> selectedScreen = screen })
                1 -> ScreenMisTareas(db.taskDao())
                2 -> ScreenRutina(db.routineDao())
                3 -> ScreenDibujar()
                4 -> ScreenUser() // Aquí se llama a tu código intacto
            }
        }
    }
}