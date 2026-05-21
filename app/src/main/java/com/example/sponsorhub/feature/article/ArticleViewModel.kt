package com.example.sponsorhub.feature.article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sponsorhub.data.model.Article
import com.example.sponsorhub.data.repository.ArticleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.content.Context
import android.net.Uri

class ArticleViewModel : ViewModel() {

    private val repository =
        ArticleRepository()

    private val _articles =
        MutableStateFlow<List<Article>>(
            emptyList()
        )

    val articles =
        _articles.asStateFlow()

    private val _article =
        MutableStateFlow<Article?>(null)

    val article =
        _article.asStateFlow()

    private val _message =
        MutableStateFlow("")

    val message =
        _message.asStateFlow()

    private val _isSuccess =
        MutableStateFlow(false)

    val isSuccess =
        _isSuccess.asStateFlow()

    fun loadArticles() {

        viewModelScope.launch {

            _articles.value =
                repository.getArticles()
        }
    }

    fun loadArticleDetail(
        articleId: String
    ) {

        viewModelScope.launch {

            _article.value =
                repository.getArticleById(
                    articleId
                )
        }
    }

    fun createArticle(
        context: Context,
        title: String,
        content: String,
        category: String,
        imageUri: Uri?
    ) {

        viewModelScope.launch {

            val result =
                repository.createArticle(
                    context,
                    title,
                    content,
                    category,
                    imageUri
                )

            if (result.isSuccess) {

                _isSuccess.value = true

                _message.value =
                    "Artikel berhasil dibuat"

            } else {

                _isSuccess.value = false

                _message.value =
                    result.exceptionOrNull()?.message
                        ?: "Artikel gagal dibuat"
            }
        }
    }

    fun resetState() {

        _isSuccess.value = false

        _message.value = ""
    }
    fun deleteArticle(
        articleId: String
    ) {

        viewModelScope.launch {

            repository.deleteArticle(
                articleId
            )
        }
    }
    fun updateArticle(
        article: Article
    ) {

        viewModelScope.launch {

            val result =
                repository.updateArticle(
                    article
                )

            if (result.isSuccess) {

                _isSuccess.value = true

                _message.value =
                    "Artikel berhasil diupdate"

            } else {

                _isSuccess.value = false

                _message.value =
                    result.exceptionOrNull()?.message
                        ?: "Artikel gagal diupdate"
            }
        }
    }
}