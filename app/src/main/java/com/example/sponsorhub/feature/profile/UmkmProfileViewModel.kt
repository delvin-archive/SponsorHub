package com.example.sponsorhub.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sponsorhub.core.network.RetrofitClient
import com.example.sponsorhub.data.model.Product
import com.example.sponsorhub.data.model.User
import com.example.sponsorhub.data.repository.FavoriteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UmkmProfileViewModel : ViewModel() {

    private val apiService = RetrofitClient.apiService
    private val favoriteRepository = FavoriteRepository()

    private val _umkm = MutableStateFlow<User?>(null)
    val umkm = _umkm.asStateFlow()

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products = _products.asStateFlow()

    private val _favoriteCount = MutableStateFlow(0)
    val favoriteCount = _favoriteCount.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    fun loadUmkmData(umkmId: String) {
        viewModelScope.launch {
            _isLoading.value = true

            _umkm.value = apiService.getUserById("eq.$umkmId").firstOrNull()
            _products.value = apiService.getProductsByUser("eq.$umkmId")
            _favoriteCount.value = favoriteRepository.getFavoriteCount(umkmId)

            _isLoading.value = false
        }
    }
}
