package com.example.sponsorhub.data.repository

import android.content.Context
import android.net.Uri
import com.example.sponsorhub.core.network.RetrofitClient
import com.example.sponsorhub.core.network.SupabaseManager
import com.example.sponsorhub.core.utils.Constants
import com.example.sponsorhub.data.model.Product
import com.example.sponsorhub.data.remote.request.CreateProductRequest
import io.github.jan.supabase.storage.storage

class ProductRepository {

    private val apiService = RetrofitClient.apiService
    private val supabaseClient = SupabaseManager.client

    suspend fun getProducts(): List<Product> {
        return try {
            apiService.getProducts()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getProductById(productId: String): Product? {
        return try {
            apiService.getProductById("eq.$productId").firstOrNull()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun createProduct(
        context: Context,
        name: String,
        description: String,
        imageUri: Uri?
    ): Result<Unit> {
        return try {
            var imageUrl: String? = null

            if (imageUri != null) {
                val bytes = context.contentResolver
                    .openInputStream(imageUri)
                    ?.readBytes()

                if (bytes != null) {
                    val fileName = "${System.currentTimeMillis()}.jpg"

                    supabaseClient.storage
                        .from(Constants.PRODUCT_BUCKET)
                        .upload(path = fileName, data = bytes) {
                            upsert = true
                        }

                    imageUrl = supabaseClient.storage
                        .from(Constants.PRODUCT_BUCKET)
                        .publicUrl(fileName)
                }
            }

            val response = apiService.createProduct(
                CreateProductRequest(
                    productName = name,
                    description = description,
                    productUrl = imageUrl
                )
            )

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Create product failed: ${response.code()} ${response.message()}"))
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun deleteProduct(productId: String): Result<Unit> {
        return try {
            val response = apiService.deleteProduct("eq.$productId")

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Delete product failed: ${response.code()} ${response.message()}"))
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}