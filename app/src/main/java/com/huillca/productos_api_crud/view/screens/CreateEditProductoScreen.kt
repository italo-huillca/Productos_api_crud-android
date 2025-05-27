package com.huillca.productos_api_crud.view.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.huillca.productos_api_crud.data.model.ProductoCreate
import com.huillca.productos_api_crud.data.model.ProductoCreateWithImage
import com.huillca.productos_api_crud.data.model.ProductoUpdate
import com.huillca.productos_api_crud.viewmodel.ProductoViewModel
import com.huillca.productos_api_crud.viewmodel.CategoriaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEditProductoScreen(
    navController: NavController,
    productoViewModel: ProductoViewModel,
    categoriaViewModel: CategoriaViewModel,
    productoId: Int? = null
) {
    val context = LocalContext.current
    val isEditing = productoId != null
    val producto by productoViewModel.productoSeleccionado
    val categorias by categoriaViewModel.categorias
    val isLoading by productoViewModel.isLoading
    val errorMessage by productoViewModel.errorMessage
    val isSuccess by productoViewModel.isSuccess

    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var categoriaSeleccionada by remember { mutableStateOf(0) }
    var expanded by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var showImageOptions by remember { mutableStateOf(false) }

    // Launcher para seleccionar imagen de galería
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    // Launcher para tomar foto con cámara
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (!success) {
            selectedImageUri = null
        }
    }

    // Cargar datos para edición
    LaunchedEffect(productoId) {
        categoriaViewModel.loadCategorias()
        if (isEditing && productoId != null) {
            productoViewModel.loadProducto(productoId)
        }
    }

    // Llenar campos cuando se carga el producto para editar
    LaunchedEffect(producto) {
        if (isEditing && producto != null) {
            nombre = producto!!.nombre
            precio = producto!!.precio
            stock = producto!!.stock.toString()
            categoriaSeleccionada = producto!!.categoria
        }
    }

    // Manejar éxito
    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            productoViewModel.clearSuccess()
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Editar Producto" else "Nuevo Producto") },
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
                label = { Text("Nombre del producto") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Campo Precio
            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it },
                label = { Text("Precio") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                leadingIcon = { Text("$") }
            )

            // Campo Stock
            OutlinedTextField(
                value = stock,
                onValueChange = { stock = it },
                label = { Text("Stock") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            // Selección de imagen (solo al crear)
            if (!isEditing) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showImageOptions = true },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    if (selectedImageUri != null) {
                        // Mostrar imagen seleccionada
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Imagen seleccionada:",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            AsyncImage(
                                model = selectedImageUri,
                                contentDescription = "Imagen seleccionada",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Toca para cambiar imagen",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    } else {
                        // Placeholder para seleccionar imagen
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.PhotoLibrary,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Seleccionar imagen",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Toca para elegir una foto",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Mostrar imagen actual solo al editar (sin posibilidad de cambiarla)
            if (isEditing && producto != null && producto!!.imagen.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Imagen actual:",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        AsyncImage(
                            model = producto!!.imagen,
                            contentDescription = "Imagen actual",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "La imagen no se puede editar desde la app",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Dropdown para Categoría
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = categorias.find { it.id == categoriaSeleccionada }?.nombre ?: "Seleccionar categoría",
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Categoría") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categorias.forEach { categoria ->
                        DropdownMenuItem(
                            text = { Text(categoria.nombre) },
                            onClick = {
                                categoriaSeleccionada = categoria.id
                                expanded = false
                            }
                        )
                    }
                }
            }




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
                        if (isEditing && productoId != null) {
                            // Actualizar producto - solo campos editables (sin imagen)
                            val productoUpdate = ProductoUpdate(
                                nombre = nombre,
                                precio = precio,
                                stock = stock.toIntOrNull() ?: 0,
                                categoria = categoriaSeleccionada
                            )
                            productoViewModel.updateProducto(productoId, productoUpdate)
                        } else {
                            // Crear nuevo producto
                            if (selectedImageUri != null) {
                                // Crear con imagen
                                val productoCreateWithImage = ProductoCreateWithImage(
                                    nombre = nombre,
                                    precio = precio,
                                    stock = stock.toIntOrNull() ?: 0,
                                    categoria = categoriaSeleccionada
                                )
                                productoViewModel.createProductoWithImage(
                                    context, 
                                    productoCreateWithImage, 
                                    selectedImageUri!!
                                )
                            } else {
                                // Crear sin imagen
                                val productoCreate = ProductoCreate(
                                    nombre = nombre,
                                    precio = precio,
                                    stock = stock.toIntOrNull() ?: 0,
                                    categoria = categoriaSeleccionada
                                )
                                productoViewModel.createProducto(productoCreate)
                            }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !isLoading && 
                              nombre.isNotBlank() && 
                              precio.isNotBlank() && 
                              stock.isNotBlank() && 
                              categoriaSeleccionada > 0
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

    // Diálogo de opciones de imagen
    if (showImageOptions) {
        AlertDialog(
            onDismissRequest = { showImageOptions = false },
            title = { Text("Seleccionar imagen") },
            text = { Text("¿Cómo quieres agregar la imagen?") },
            confirmButton = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextButton(
                        onClick = {
                            showImageOptions = false
                            galleryLauncher.launch("image/*")
                        }
                    ) {
                        Icon(Icons.Default.PhotoLibrary, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Galería")
                    }
                    TextButton(
                        onClick = {
                            showImageOptions = false
                            // Para cámara necesitaríamos crear un URI temporal
                            // Por simplicidad, solo usamos galería por ahora
                            galleryLauncher.launch("image/*")
                        }
                    ) {
                        Icon(Icons.Default.PhotoCamera, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Cámara")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { showImageOptions = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
} 