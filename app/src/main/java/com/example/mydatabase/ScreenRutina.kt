package com.example.mydatabase

import androidx.compose.animation.AnimatedVisibility
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
fun ScreenRutina(dao: RoutineDao) {
    val coroutineScope = rememberCoroutineScope()
    var routines by remember { mutableStateOf(listOf<Routine>()) }
    var showDialog by remember { mutableStateOf(false) }
    var editingRoutine by remember { mutableStateOf<Routine?>(null) }
    var newTitle by remember { mutableStateOf("") }
    var newSteps by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        routines = dao.getAllRoutines()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editingRoutine = null
                    newTitle = ""
                    newSteps = ""
                    showDialog = true
                },
                containerColor = Color(0xFFF27B50),
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar Rutina")
            }
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(routines) { routine ->
                RoutineCard(
                    routine = routine,
                    onEdit = {
                        editingRoutine = routine
                        newTitle = routine.title
                        newSteps = routine.steps
                        showDialog = true
                    },
                    onDelete = {
                        coroutineScope.launch {
                            dao.deleteRoutine(routine)
                            routines = dao.getAllRoutines()
                        }
                    }
                )
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(if (editingRoutine == null) "Nueva Rutina" else "Editar Rutina") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = newTitle,
                            onValueChange = { newTitle = it },
                            label = { Text("Actividad (Ej: Lavarse las manos)") },
                            singleLine = true
                        )
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(
                            value = newSteps,
                            onValueChange = { newSteps = it },
                            label = { Text("Instrucciones") },
                            minLines = 3,
                            maxLines = 5
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (newTitle.isNotBlank() && newSteps.isNotBlank()) {
                            coroutineScope.launch {
                                if (editingRoutine == null) {
                                    dao.insertRoutine(Routine(title = newTitle, steps = newSteps))
                                } else {
                                    dao.updateRoutine(editingRoutine!!.copy(title = newTitle, steps = newSteps))
                                }
                                routines = dao.getAllRoutines()
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
fun RoutineCard(routine: Routine, onEdit: () -> Unit, onDelete: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = routine.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Filled.Edit, contentDescription = "Editar", tint = Color.Gray)
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Filled.Delete, contentDescription = "Eliminar", tint = Color.Gray)
                    }
                }
            }
            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    Text("Instrucciones:", fontWeight = FontWeight.SemiBold, color = Color.DarkGray)
                    Text(text = routine.steps, color = Color.Black, modifier = Modifier.padding(top = 4.dp))
                }
            }
        }
    }
}