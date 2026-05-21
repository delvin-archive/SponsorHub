package com.example.sponsorhub.data.repository

import android.content.Context
import android.net.Uri
import com.example.sponsorhub.core.network.SupabaseManager
import com.example.sponsorhub.core.utils.Constants
import com.example.sponsorhub.data.model.Product
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage

class ProductRepository {

    private val client =
        SupabaseManager.client

    suspend fun getProducts(): List<Product> {

        return try {

            client
                .from("products")
                .select()
                .decodeList<Product>()

        } catch (e: Exception) {

            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getProductById(
        productId: String
    ): Product? {

        return try {

            client
                .from("products")
                .select {
                    filter {
                        eq(
                            "id",
                            productId
                        )
                    }
                }
                .decodeList<Product>()
                .firstOrNull()

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

            var imageUrl: String? =
                null

            if (imageUri != null) {

                val bytes =
                    context.contentResolver
                        .openInputStream(
                            imageUri
                        )
                        ?.readBytes()

                if (bytes != null) {

                    val fileName =
                        "${System.currentTimeMillis()}.jpg"

                    client.storage
                        .from(
                            Constants.PRODUCT_BUCKET
                        )
                        .upload(
                            path = fileName,
                            data = bytes
                        ) {
                            upsert = true
                        }

                    imageUrl =
                        client.storage
                            .from(
                                Constants.PRODUCT_BUCKET
                            )
                            .publicUrl(
                                fileName
                            )
                }
            }

            val product =
                Product(
                    productName = name,
                    description =
                        description,
                    productUrl =
                        imageUrl
                )

            client
                .from("products")
                .insert(product)

            Result.success(Unit)

        } catch (e: Exception) {

            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun deleteProduct(
        productId: String
    ): Result<Unit> {

        return try {

            client
                .from("products")
                .delete {
                    filter {
                        eq(
                            "id",
                            productId
                        )
                    }
                }

            Result.success(Unit)

        } catch (e: Exception) {

            e.printStackTrace()
            Result.failure(e)
        }
    }
}