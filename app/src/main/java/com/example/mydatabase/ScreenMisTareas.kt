package com.example.mydatabase

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenMisTareas(dao: TaskDao) {
    val coroutineScope = rememberCoroutineScope()
    var tasks by remember { mutableStateOf(listOf<Task>()) }
    var showDialog by remember { mutableStateOf(false) }
    var newTaskDescription by remember { mutableStateOf("") }

    // Cargar las tareas al iniciar la pantalla
    LaunchedEffect(Unit) {
        tasks = dao.getAllTasks()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Mis Tareas", fontWeight = FontWeight.Bold, color = Color.White)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF27B50) // El color naranja de tu topbar
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = Color(0xFFF27B50),
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar Tarea")
            }
        },
        containerColor = Color(0xFFFAF0E6) // El color de fondo claro de tu diseño
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(tasks) { task ->
                TaskCard(
                    task = task,
                    onToggleStatus = {
                        coroutineScope.launch {
                            val updatedTask = task.copy(isPending = !task.isPending)
                            dao.updateTask(updatedTask) // UPDATE
                            tasks = dao.getAllTasks()   // READ
                        }
                    },
                    onDelete = {
                        coroutineScope.launch {
                            dao.deleteTask(task)      // DELETE
                            tasks = dao.getAllTasks() // READ
                        }
                    }
                )
            }
        }

        // Diálogo para agregar una nueva tarea (CREATE)
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Nueva Tarea") },
                text = {
                    OutlinedTextField(
                        value = newTaskDescription,
                        onValueChange = { newTaskDescription = it },
                        label = { Text("Descripción") },
                        singleLine = true
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (newTaskDescription.isNotBlank()) {
                            coroutineScope.launch {
                                dao.insertTask(Task(description = newTaskDescription)) // CREATE
                                tasks = dao.getAllTasks()                              // READ
                                newTaskDescription = ""
                                showDialog = false
                            }
                        }
                    }) {
                        Text("Guardar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

@Composable
fun TaskCard(task: Task, onToggleStatus: () -> Unit, onDelete: () -> Unit) {
    // Lógica de colores inspirada en tu prototipo
    val cardColor = if (task.isPending) Color(0xFFF19C90) else Color(0xFFA5E66B)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggleStatus() }, // Al hacer clic, cambia de pendiente a completada
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = task.description,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Eliminar",
                    tint = Color.DarkGray
                )
            }
        }
    }
}