package com.example.mydatabase

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenUser() {
    val context = LocalContext.current
    val db = remember { crearDatabase(context) }
    val dao = db.userDao()
    val coroutineScope = rememberCoroutineScope()

    var id by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var dataUser by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Usuarios") },
                actions = {
                    TextButton(onClick = {
                        val user = User(0, firstName, lastName)
                        coroutineScope.launch {
                            AgregarUsuario(user = user, dao = dao)
                            firstName = ""
                            lastName = ""
                            dataUser = getUsers(dao = dao)
                        }
                    }) {
                        Text("Agregar")
                    }

                    TextButton(onClick = {
                        coroutineScope.launch {
                            dataUser = getUsers(dao = dao)
                        }
                    }) {
                        Text("Listar")
                    }

                    TextButton(onClick = {
                        coroutineScope.launch {
                            dao.deleteLast()
                            dataUser = getUsers(dao = dao)
                        }
                    }) {
                        Text("Eliminar")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            TextField(
                value = id,
                onValueChange = { id = it },
                label = { Text("ID (solo lectura)") },
                readOnly = true,
                singleLine = true
            )
            Spacer(Modifier.height(8.dp))
            TextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name") },
                singleLine = true
            )
            Spacer(Modifier.height(8.dp))
            TextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name") },
                singleLine = true
            )
            Spacer(Modifier.height(20.dp))
            Text(
                text = dataUser,
                fontSize = 20.sp
            )
        }
    }
}

fun crearDatabase(context: Context): UserDatabase {
    return Room.databaseBuilder(
        context,
        UserDatabase::class.java,
        "user_db"
    ).build()
}

suspend fun getUsers(dao: UserDao): String {
    var rpta = ""
    val users = dao.getAll()
    users.forEach { user ->
        val fila = "${user.firstName} - ${user.lastName}\n"
        rpta += fila
    }
    return rpta
}

suspend fun AgregarUsuario(user: User, dao: UserDao) {
    try {
        dao.insert(user)
    } catch (e: Exception) {
        Log.e("User", "Error insert: ${e.message}")
    }
}