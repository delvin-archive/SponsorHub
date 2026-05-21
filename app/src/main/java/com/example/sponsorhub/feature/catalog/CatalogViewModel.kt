package com.example.sponsorhub.feature.catalog

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sponsorhub.data.model.Product
import com.example.sponsorhub.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CatalogViewModel : ViewModel() {

    private val repository =
        ProductRepository()

    // PRODUCT LIST
    private val _products =
        MutableStateFlow<List<Product>>(
            emptyList()
        )

    val products =
        _products.asStateFlow()

    // SELECTED PRODUCT
    private val _selectedProduct =
        MutableStateFlow<Product?>(
            null
        )

    val selectedProduct =
        _selectedProduct.asStateFlow()

    // SUCCESS STATE
    private val _isSuccess =
        MutableStateFlow(false)

    val isSuccess =
        _isSuccess.asStateFlow()

    // MESSAGE
    private val _message =
        MutableStateFlow("")

    val message =
        _message.asStateFlow()

    fun loadProducts() {

        viewModelScope.launch {

            _products.value =
                repository
                    .getProducts()
        }
    }

    fun loadProductById(
        productId: String
    ) {

        viewModelScope.launch {

            _selectedProduct.value =
                repository
                    .getProductById(
                        productId
                    )
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
                repository
                    .createProduct(
                        context =
                            context,
                        name = name,
                        description =
                            description,
                        imageUri =
                            imageUri
                    )

            result.fold(

                onSuccess = {

                    _isSuccess.value =
                        true

                    _message.value =
                        "Produk berhasil ditambahkan"

                    loadProducts()
                },

                onFailure = {

                    _isSuccess.value =
                        false

                    _message.value =
                        it.message
                            ?: "Gagal menambahkan produk"
                }
            )
        }
    }

    fun deleteProduct(
        productId: String
    ) {

        viewModelScope.launch {

            val result =
                repository
                    .deleteProduct(
                        productId
                    )

            result.fold(

                onSuccess = {

                    _message.value =
                        "Produk berhasil dihapus"

                    loadProducts()
                },

                onFailure = {

                    _message.value =
                        it.message
                            ?: "Gagal menghapus produk"
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