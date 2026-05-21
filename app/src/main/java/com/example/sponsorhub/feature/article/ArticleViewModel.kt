package com.example.sponsorhub.feature.article

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sponsorhub.data.model.Article
import com.example.sponsorhub.data.repository.ArticleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ArticleViewModel : ViewModel() {

    private val repository =
        ArticleRepository()

    private val _articles =
        MutableStateFlow<List<Article>>(
            emptyList()
        )

    val articles =
        _articles.asStateFlow()

    private val _selectedArticle =
        MutableStateFlow<Article?>(
            null
        )

    val selectedArticle =
        _selectedArticle.asStateFlow()

    private val _isSuccess =
        MutableStateFlow(false)

    val isSuccess =
        _isSuccess.asStateFlow()

    private val _message =
        MutableStateFlow("")

    val message =
        _message.asStateFlow()

    fun loadArticles() {

        viewModelScope.launch {

            _articles.value =
                repository
                    .getArticles()
        }
    }

    fun loadArticleById(
        articleId: String
    ) {

        viewModelScope.launch {

            _selectedArticle.value =
                repository
                    .getArticleById(
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
                repository
                    .createArticle(
                        context =
                            context,
                        title = title,
                        content =
                            content,
                        category =
                            category,
                        imageUri =
                            imageUri
                    )

            result.fold(

                onSuccess = {

                    _isSuccess.value =
                        true

                    _message.value =
                        "Artikel berhasil dibuat"

                    loadArticles()
                },

                onFailure = {

                    _isSuccess.value =
                        false

                    _message.value =
                        it.message
                            ?: "Gagal membuat artikel"
                }
            )
        }
    }

    fun deleteArticle(
        articleId: String
    ) {

        viewModelScope.launch {

            val result =
                repository
                    .deleteArticle(
                        articleId
                    )

            result.fold(

                onSuccess = {

                    _message.value =
                        "Artikel berhasil dihapus"

                    loadArticles()
                },

                onFailure = {

                    _message.value =
                        it.message
                            ?: "Gagal menghapus artikel"
                }
            )
        }
    }

    fun resetState() {

        _isSuccess.value =
            false

        _message.value =
            ""
    }
}