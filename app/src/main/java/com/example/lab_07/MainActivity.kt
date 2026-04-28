package com.example.lab_07

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.lab_07.ui.theme.Lab_07Theme
import com.example.mydatabase.ScreenUser

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab_07Theme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ScreenUser()
                }
            }
        }
    }
}