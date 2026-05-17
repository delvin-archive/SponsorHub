package com.example.sponsorhub.data.repository

import com.example.sponsorhub.core.network.SupabaseManager
import com.example.sponsorhub.data.model.Article
import io.github.jan.supabase.postgrest.from

class ArticleRepository {

    private val client = SupabaseManager.client

    suspend fun getArticles(): List<Article> {

        return try {

            client
                .from("articles")
                .select()
                .decodeList<Article>()

        } catch (e: Exception) {

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

            null
        }
    }

    suspend fun createArticle(
        article: Article
    ): Result<Unit> {

        return try {

            client
                .from("articles")
                .insert(article)

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}