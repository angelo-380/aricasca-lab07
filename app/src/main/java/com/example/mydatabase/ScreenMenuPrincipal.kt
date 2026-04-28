package com.example.mydatabase

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ScreenMenuPrincipal(onNavigate: (Int) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(Color(0xFFF3E57B))
                    .clickable { onNavigate(1) },
                contentAlignment = Alignment.Center
            ) {
                Text("Mis tareas", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
            }

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF7CB8E6))
                    .clickable { onNavigate(2) },
                contentAlignment = Alignment.Center
            ) {
                Text("Rutina", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Nuevo botón para Dibujar
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(Color(0xFFF19C90))
                .clickable { onNavigate(3) },
            contentAlignment = Alignment.Center
        ) {
            Text("Dibujar", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
        }
    }
}