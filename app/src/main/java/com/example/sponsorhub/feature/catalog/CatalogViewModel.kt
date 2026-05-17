package com.example.sponsorhub.feature.catalog

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sponsorhub.data.model.Product
import com.example.sponsorhub.data.model.User
import com.example.sponsorhub.data.repository.AuthRepository
import com.example.sponsorhub.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CatalogViewModel : ViewModel() {

    private val productRepository =
        ProductRepository()

    private val authRepository =
        AuthRepository()

    private val _products =
        MutableStateFlow<List<Product>>(
            emptyList()
        )

    val products =
        _products.asStateFlow()

    private val _user =
        MutableStateFlow<User?>(null)

    val user =
        _user.asStateFlow()

    private val _message =
        MutableStateFlow("")

    val message =
        _message.asStateFlow()

    private val _isSuccess =
        MutableStateFlow(false)

    val isSuccess =
        _isSuccess.asStateFlow()

    fun loadCatalog() {

        viewModelScope.launch {

            _user.value =
                authRepository
                    .getCurrentUser()

            _products.value =
                productRepository
                    .getMyProducts()
        }
    }

    fun createProduct(
        context: Context,
        name: String,
        description: String,
        imageUri: Uri?
    ) {

        viewModelScope.launch {

            val result =
                productRepository
                    .createProduct(
                        context,
                        name,
                        description,
                        imageUri
                    )

            if (result.isSuccess) {

                _isSuccess.value = true

                _message.value =
                    "Produk berhasil dibuat"

                loadCatalog()

            } else {

                _isSuccess.value = false

                _message.value =
                    result.exceptionOrNull()?.message
                        ?: "Produk gagal dibuat"
            }
        }
    }

    fun resetState() {

        _isSuccess.value = false

        _message.value = ""
    }
}