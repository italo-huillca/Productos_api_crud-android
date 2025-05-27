package com.huillca.productos_api_crud.util

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object FileUtils {
    
    fun uriToMultipartBody(context: Context, uri: Uri, parameterName: String): MultipartBody.Part? {
        return try {
            val file = uriToFile(context, uri)
            val requestBody = file.asRequestBody("image/*".toMediaType())
            MultipartBody.Part.createFormData(parameterName, file.name, requestBody)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    private fun uriToFile(context: Context, uri: Uri): File {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val fileName = "temp_image_${System.currentTimeMillis()}.jpg"
        val tempFile = File(context.cacheDir, fileName)
        
        inputStream?.use { input ->
            FileOutputStream(tempFile).use { output ->
                input.copyTo(output)
            }
        }
        
        return tempFile
    }
    
    fun createRequestBody(value: String) = 
        okhttp3.RequestBody.create("text/plain".toMediaType(), value)
    
    fun getCurrentDateTime(): String {
        return Instant.now()
            .atOffset(ZoneOffset.UTC)
            .format(DateTimeFormatter.ISO_INSTANT)
    }
} 