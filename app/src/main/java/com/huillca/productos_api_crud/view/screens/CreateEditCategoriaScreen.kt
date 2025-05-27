package com.huillca.productos_api_crud.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.huillca.productos_api_crud.data.model.CategoriaCreate
import com.huillca.productos_api_crud.data.model.CategoriaUpdate
import com.huillca.productos_api_crud.viewmodel.CategoriaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEditCategoriaScreen(
    navController: NavController,
    viewModel: CategoriaViewModel,
    categoriaId: Int? = null
) {
    val isEditing = categoriaId != null
    val categoria by viewModel.categoriaSeleccionada
    val isLoading by viewModel.isLoading
    val errorMessage by viewModel.errorMessage
    val isSuccess by viewModel.isSuccess

    var nombre by remember { mutableStateOf("") }
    var imagen by remember { mutableStateOf("") }

    // Cargar datos para edición
    LaunchedEffect(categoriaId) {
        if (isEditing && categoriaId != null) {
            viewModel.loadCategoria(categoriaId)
        }
    }

    // Llenar campos cuando se carga la categoría para editar
    LaunchedEffect(categoria) {
        if (isEditing && categoria != null) {
            nombre = categoria!!.nombre
            imagen = categoria!!.imagen
        }
    }

    // Manejar éxito
    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            viewModel.clearSuccess()
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Editar Categoría" else "Nueva Categoría") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Campo Nombre
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre de la categoría") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Campo Imagen
            OutlinedTextField(
                value = imagen,
                onValueChange = { imagen = it },
                label = { Text("URL de la imagen") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Mostrar error si existe
            errorMessage?.let { message ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = message,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.weight(1f),
                    enabled = !isLoading
                ) {
                    Text("Cancelar")
                }

                Button(
                    onClick = {
                        if (isEditing && categoriaId != null) {
                            // Actualizar categoría
                            val categoriaUpdate = CategoriaUpdate(
                                nombre = nombre.takeIf { it.isNotBlank() },
                                imagen = imagen.takeIf { it.isNotBlank() }
                            )
                            viewModel.updateCategoria(categoriaId, categoriaUpdate)
                        } else {
                            // Crear nueva categoría
                            val categoriaCreate = CategoriaCreate(
                                nombre = nombre,
                                imagen = imagen.takeIf { it.isNotBlank() }
                            )
                            viewModel.createCategoria(categoriaCreate)
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !isLoading && nombre.isNotBlank()
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(if (isEditing) "Actualizar" else "Crear")
                    }
                }
            }
        }
    }
} 