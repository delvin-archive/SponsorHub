package com.example.sponsorhub.data.repository

import android.content.Context
import android.net.Uri
import com.example.sponsorhub.core.network.SupabaseManager
import com.example.sponsorhub.core.utils.Constants
import com.example.sponsorhub.data.model.Article
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage

class ArticleRepository {

    private val client =
        SupabaseManager.client

    suspend fun getArticles(): List<Article> {

        return try {

            client
                .from("articles")
                .select()
                .decodeList<Article>()

        } catch (e: Exception) {

            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getArticleById(
        articleId: String
    ): Article? {

        return try {

            client
                .from("articles")
                .select {
                    filter {
                        eq("id", articleId)
                    }
                }
                .decodeList<Article>()
                .firstOrNull()

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

            var imageUrl: String? =
                null

            if (imageUri != null) {

                val bytes =
                    context.contentResolver
                        .openInputStream(imageUri)
                        ?.readBytes()

                if (bytes != null) {

                    val fileName =
                        "${System.currentTimeMillis()}.jpg"

                    client.storage
                        .from(
                            Constants.ARTICLE_BUCKET
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
                                Constants.ARTICLE_BUCKET
                            )
                            .publicUrl(fileName)
                }
            }

            val article =
                Article(
                    title = title,
                    content = content,
                    category = category,
                    imageUrl = imageUrl
                )

            client
                .from("articles")
                .insert(article)

            Result.success(Unit)

        } catch (e: Exception) {

            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun deleteArticle(
        articleId: String
    ): Result<Unit> {

        return try {

            client
                .from("articles")
                .delete {
                    filter {
                        eq(
                            "id",
                            articleId
                        )
                    }
                }

            Result.success(Unit)

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