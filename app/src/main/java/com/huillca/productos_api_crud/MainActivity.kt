package com.huillca.productos_api_crud

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.huillca.productos_api_crud.navigation.NavGraph
import com.huillca.productos_api_crud.ui.theme.Productos_api_crudTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Productos_api_crudTheme {
                ProductosApp()
            }
        }
    }
}

@Composable
fun ProductosApp() {
    val navController = rememberNavController()
    NavGraph(navController = navController)
}

@Preview(showBackground = true)
@Composable
fun ProductosAppPreview() {
    Productos_api_crudTheme {
        ProductosApp()
    }
}