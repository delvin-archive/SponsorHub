package com.example.sponsorhub.data.repository

import android.content.Context
import android.net.Uri
import com.example.sponsorhub.core.network.SupabaseManager
import com.example.sponsorhub.data.model.Product
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage

class ProductRepository {

    private val client = SupabaseManager.client

    suspend fun getMyProducts(): List<Product> {

        return try {

            val userId =
                client.auth.currentUserOrNull()?.id
                    ?: return emptyList()

            client
                .from("products")
                .select {

                    filter {

                        eq("user_id", userId)
                    }
                }
                .decodeList<Product>()

        } catch (e: Exception) {

            emptyList()
        }
    }

    suspend fun createProduct(
        context: Context,
        name: String,
        description: String,
        imageUri: Uri?
    ): Result<Unit> {

        return try {

            val userId =
                client.auth.currentUserOrNull()?.id
                    ?: throw Exception("User not found")

            var imageUrl: String? = null

            if (imageUri != null) {

                val bytes =
                    context.contentResolver
                        .openInputStream(imageUri)
                        ?.readBytes()

                if (bytes != null) {

                    val fileName =
                        "${System.currentTimeMillis()}.jpg"

                    client.storage
                        .from("product_images")
                        .upload(
                            path = fileName,
                            data = bytes
                        ) {

                            upsert = true
                        }

                    imageUrl =
                        client.storage
                            .from("product_images")
                            .publicUrl(fileName)
                }
            }

            val product = Product(

                userId = userId,

                productName = name,

                description = description,

                productUrl = imageUrl
            )

            client
                .from("products")
                .insert(product)

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}