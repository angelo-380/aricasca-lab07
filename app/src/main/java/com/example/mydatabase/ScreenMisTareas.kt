package com.example.mydatabase

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun ScreenMisTareas(dao: TaskDao) {
    val coroutineScope = rememberCoroutineScope()
    var tasks by remember { mutableStateOf(listOf<Task>()) }
    var showDialog by remember { mutableStateOf(false) }
    var newTaskDescription by remember { mutableStateOf("") }
    var editingTask by remember { mutableStateOf<Task?>(null) }

    LaunchedEffect(Unit) {
        tasks = dao.getAllTasks()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editingTask = null
                    newTaskDescription = ""
                    showDialog = true
                },
                containerColor = Color(0xFFF27B50),
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar Tarea")
            }
        },
        containerColor = Color.Transparent
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(tasks) { task ->
                TaskCard(
                    task = task,
                    onToggleStatus = {
                        coroutineScope.launch {
                            dao.updateTask(task.copy(isPending = !task.isPending))
                            tasks = dao.getAllTasks()
                        }
                    },
                    onEdit = {
                        editingTask = task
                        newTaskDescription = task.description
                        showDialog = true
                    },
                    onDelete = {
                        coroutineScope.launch {
                            dao.deleteTask(task)
                            tasks = dao.getAllTasks()
                        }
                    }
                )
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(if (editingTask == null) "Nueva Tarea" else "Editar Tarea") },
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
                                if (editingTask == null) {
                                    dao.insertTask(Task(description = newTaskDescription))
                                } else {
                                    dao.updateTask(editingTask!!.copy(description = newTaskDescription))
                                }
                                tasks = dao.getAllTasks()
                                showDialog = false
                            }
                        }
                    }) {
                        Text("Guardar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) { Text("Cancelar") }
                }
            )
        }
    }
}

@Composable
fun TaskCard(task: Task, onToggleStatus: () -> Unit, onEdit: () -> Unit, onDelete: () -> Unit) {
    val cardColor = if (task.isPending) Color(0xFFF19C90) else Color(0xFFA5E66B)

    Card(
        modifier = Modifier.fillMaxWidth().clickable { onToggleStatus() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = task.description,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )
            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Filled.Edit, contentDescription = "Editar", tint = Color.DarkGray)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Filled.Delete, contentDescription = "Eliminar", tint = Color.DarkGray)
                }
            }
        }
    }
}