package com.example.mydatabase

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class LinePath(val path: Path, val color: Color, val strokeWidth: Float)

@Composable
fun ScreenDibujar() {
    var lines by remember { mutableStateOf(listOf<LinePath>()) }
    var currentPath by remember { mutableStateOf<Path?>(null) }
    var currentColor by remember { mutableStateOf(Color.Black) }
    // Corregido el aviso de mutableFloatStateOf
    var currentStroke by remember { mutableFloatStateOf(5f) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                currentPath = Path().apply { moveTo(offset.x, offset.y) }
                            },
                            onDrag = { change, _ ->
                                currentPath?.lineTo(change.position.x, change.position.y)
                                currentPath = Path().apply { addPath(currentPath!!) }
                            },
                            onDragEnd = {
                                currentPath?.let {
                                    lines = lines + LinePath(it, currentColor, currentStroke)
                                }
                                currentPath = null
                            }
                        )
                    }
            ) {
                lines.forEach { line ->
                    drawPath(
                        path = line.path,
                        color = line.color,
                        style = Stroke(width = line.strokeWidth, cap = StrokeCap.Round, join = StrokeJoin.Round)
                    )
                }
                currentPath?.let {
                    drawPath(
                        path = it,
                        color = currentColor,
                        style = Stroke(width = currentStroke, cap = StrokeCap.Round, join = StrokeJoin.Round)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFFB3D9E6))
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Herramienta Lápiz
            IconButton(onClick = { currentColor = Color.Black }) {
                Icon(Icons.Filled.Create, contentDescription = "Lápiz", tint = if (currentColor == Color.Black) Color.DarkGray else Color.Gray)
            }
            // Herramienta Borrador (pinta de blanco)
            IconButton(onClick = { currentColor = Color.White }) {
                Icon(Icons.Filled.Clear, contentDescription = "Borrador", tint = if (currentColor == Color.White) Color.DarkGray else Color.Gray)
            }
            // Herramienta Color (Círculo interactivo)
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(if (currentColor == Color.Black || currentColor == Color.White) Color.Red else Color.Blue)
                    .clickable {
                        currentColor = if (currentColor == Color.Black || currentColor == Color.White) Color.Red else Color.Blue
                    }
            )
            // Herramienta Grosor
            TextButton(onClick = { currentStroke = if (currentStroke == 5f) 15f else 5f }) {
                Text(if (currentStroke == 5f) "Fino" else "Grueso", color = Color.DarkGray, fontWeight = FontWeight.Bold)
            }
        }
    }
}