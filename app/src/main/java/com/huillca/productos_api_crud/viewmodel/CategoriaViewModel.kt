package com.huillca.productos_api_crud.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huillca.productos_api_crud.data.model.*
import com.huillca.productos_api_crud.data.repository.CategoriaRepository
import kotlinx.coroutines.launch

class CategoriaViewModel : ViewModel() {
    private val repository = CategoriaRepository()
    
    private val _categorias = mutableStateOf<List<Categoria>>(emptyList())
    val categorias: State<List<Categoria>> = _categorias
    
    private val _categoriaSeleccionada = mutableStateOf<Categoria?>(null)
    val categoriaSeleccionada: State<Categoria?> = _categoriaSeleccionada
    
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading
    
    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage
    
    private val _isSuccess = mutableStateOf(false)
    val isSuccess: State<Boolean> = _isSuccess
    
    init {
        loadCategorias()
    }
    
    fun loadCategorias() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            repository.getCategorias().collect { result ->
                _isLoading.value = false
                result.fold(
                    onSuccess = { categorias ->
                        _categorias.value = categorias
                    },
                    onFailure = { exception ->
                        _errorMessage.value = exception.message
                    }
                )
            }
        }
    }
    
    fun loadCategoria(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            repository.getCategoria(id).collect { result ->
                _isLoading.value = false
                result.fold(
                    onSuccess = { categoria ->
                        _categoriaSeleccionada.value = categoria
                    },
                    onFailure = { exception ->
                        _errorMessage.value = exception.message
                    }
                )
            }
        }
    }
    
    fun createCategoria(categoria: CategoriaCreate) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _isSuccess.value = false
            
            repository.createCategoria(categoria).collect { result ->
                _isLoading.value = false
                result.fold(
                    onSuccess = { nuevaCategoria ->
                        _isSuccess.value = true
                        loadCategorias() // Recargar la lista
                    },
                    onFailure = { exception ->
                        _errorMessage.value = exception.message
                    }
                )
            }
        }
    }
    
    fun updateCategoria(id: Int, categoria: CategoriaUpdate) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _isSuccess.value = false
            
            repository.updateCategoria(id, categoria).collect { result ->
                _isLoading.value = false
                result.fold(
                    onSuccess = { categoriaActualizada ->
                        _isSuccess.value = true
                        _categoriaSeleccionada.value = categoriaActualizada
                        loadCategorias() // Recargar la lista
                    },
                    onFailure = { exception ->
                        _errorMessage.value = exception.message
                    }
                )
            }
        }
    }
    
    fun deleteCategoria(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            repository.deleteCategoria(id).collect { result ->
                _isLoading.value = false
                result.fold(
                    onSuccess = {
                        loadCategorias() // Recargar la lista
                    },
                    onFailure = { exception ->
                        _errorMessage.value = exception.message
                    }
                )
            }
        }
    }
    
    fun clearError() {
        _errorMessage.value = null
    }
    
    fun clearSuccess() {
        _isSuccess.value = false
    }
    
    fun clearSelectedCategoria() {
        _categoriaSeleccionada.value = null
    }
} 