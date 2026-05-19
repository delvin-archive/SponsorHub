package com.example.sponsorhub.feature.article

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

    /*
    LIST ARTICLE
     */
    private val _articles =
        MutableStateFlow<List<Article>>(
            emptyList()
        )

    val articles =
        _articles.asStateFlow()

    /*
    DETAIL ARTICLE
     */
    private val _article =
        MutableStateFlow<Article?>(null)

    val article =
        _article.asStateFlow()

    /*
    MESSAGE
     */
    private val _message =
        MutableStateFlow("")

    val message =
        _message.asStateFlow()

    /*
    SUCCESS
     */
    private val _isSuccess =
        MutableStateFlow(false)

    val isSuccess =
        _isSuccess.asStateFlow()

    /*
    LOAD ALL ARTICLES
     */
    fun loadArticles() {

        viewModelScope.launch {

            _articles.value =
                repository.getArticles()
        }
    }

    /*
    LOAD DETAIL
     */
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

    /*
    CREATE ARTICLE
     */
    fun createArticle(
        article: Article
    ) {

        viewModelScope.launch {

            val result =
                repository.createArticle(
                    article
                )

            if (result.isSuccess) {

                loadArticles()

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

    /*
    UPDATE ARTICLE
     */
    fun updateArticle(
        updatedArticle: Article
    ) {

        viewModelScope.launch {

            val result =
                repository.updateArticle(
                    updatedArticle
                )

            if (result.isSuccess) {

                loadArticles()

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

    /*
    DELETE ARTICLE
     */
    fun deleteArticle(
        articleId: String
    ) {

        viewModelScope.launch {

            val result =
                repository.deleteArticle(
                    articleId
                )

            if (result.isSuccess) {

                loadArticles()

                _message.value =
                    "Artikel berhasil dihapus"

            } else {

                _message.value =
                    result.exceptionOrNull()?.message
                        ?: "Artikel gagal dihapus"
            }
        }
    }

    /*
    RESET STATE
     */
    fun resetState() {

        _isSuccess.value = false
        _message.value = ""
    }
}