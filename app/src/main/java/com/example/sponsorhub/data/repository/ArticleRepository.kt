package com.example.sponsorhub.data.repository

import android.content.Context
import android.net.Uri
import com.example.sponsorhub.core.network.RetrofitClient
import com.example.sponsorhub.core.network.SupabaseManager
import com.example.sponsorhub.core.network.SupabaseManager.client
import com.example.sponsorhub.core.utils.Constants
import com.example.sponsorhub.data.model.Article
import com.example.sponsorhub.data.remote.request.CreateArticleRequest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage

class ArticleRepository {

    private val apiService = RetrofitClient.apiService
    private val supabaseClient = SupabaseManager.client

    suspend fun getArticles(): List<Article> {
        return try {
            apiService.getArticles()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getArticleById(articleId: String): Article? {
        return try {
            apiService.getArticleById("eq.$articleId").firstOrNull()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun createArticle(
        context: Context,
        title: String,
        content: String,
        category: String,
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
                        .from(Constants.ARTICLE_BUCKET)
                        .upload(path = fileName, data = bytes) {
                            upsert = true
                        }

                    imageUrl = supabaseClient.storage
                        .from(Constants.ARTICLE_BUCKET)
                        .publicUrl(fileName)
                }
            }

            val response = apiService.createArticle(
                CreateArticleRequest(
                    title = title,
                    content = content,
                    category = category,
                    imageUrl = imageUrl
                )
            )

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Create article failed: ${response.code()} ${response.message()}"))
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun deleteArticle(articleId: String): Result<Unit> {
        return try {
            val response = apiService.deleteArticle("eq.$articleId")

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Delete article failed: ${response.code()} ${response.message()}"))
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
    suspend fun updateArticle(
        article: Article
    ): Result<Unit> {

        return try {

            client
                .from("articles")
                .update(

                    {
                        set(
                            "title",
                            article.title
                        )

                        set(
                            "content",
                            article.content
                        )

                        set(
                            "category",
                            article.category
                        )
                    }

                ) {

                    filter {

                        eq(
                            "id",
                            article.id
                        )
                    }
                }

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}