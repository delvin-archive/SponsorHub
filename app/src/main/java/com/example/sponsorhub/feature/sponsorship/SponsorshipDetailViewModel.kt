package com.example.sponsorhub.feature.sponsorship

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sponsorhub.core.network.RetrofitClient
import com.example.sponsorhub.data.model.SponsorshipRequest
import com.example.sponsorhub.data.model.User
import com.example.sponsorhub.data.repository.FavoriteRepository
import com.example.sponsorhub.data.repository.SponsorshipRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SponsorshipDetailViewModel : ViewModel() {

    private val apiService = RetrofitClient.apiService
    private val sponsorshipRepository = SponsorshipRepository()
    private val favoriteRepository = FavoriteRepository()

    private val _request = MutableStateFlow<SponsorshipRequest?>(null)
    val request = _request.asStateFlow()

    private val _umkm = MutableStateFlow<User?>(null)
    val umkm = _umkm.asStateFlow()

    private val _isAlreadyFavorited = MutableStateFlow(false)
    val isAlreadyFavorited = _isAlreadyFavorited.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _message = MutableStateFlow("")
    val message = _message.asStateFlow()

    fun loadData(sponsorshipId: String) {
        viewModelScope.launch {
            _isLoading.value = true

            val req = apiService.getSponsorshipById("eq.$sponsorshipId").firstOrNull()
            _request.value = req

            req?.umkmId?.let { umkmId ->
                _umkm.value = apiService.getUserById("eq.$umkmId").firstOrNull()
                _isAlreadyFavorited.value = favoriteRepository.isAlreadyFavorited(umkmId)
            }

            _isLoading.value = false
        }
    }

    fun updateStatus(requestId: String, status: String) {
        viewModelScope.launch {
            val result = sponsorshipRepository.updateRequestStatus(requestId, status)
            result.fold(
                onSuccess = {
                    _request.value = _request.value?.copy(status = status)
                    _message.value = if (status == "diterima") "Pengajuan berhasil diterima" else "Pengajuan ditolak"
                },
                onFailure = {
                    _message.value = it.message ?: "Gagal mengubah status"
                }
            )
        }
    }

    fun addToFavorite(umkmId: String) {
        viewModelScope.launch {
            val result = favoriteRepository.addFavorite(umkmId)
            result.fold(
                onSuccess = {
                    _isAlreadyFavorited.value = true
                    _message.value = "UMKM ditambahkan ke favorit!"
                },
                onFailure = {
                    _message.value = it.message ?: "Gagal menambahkan ke favorit"
                }
            )
        }
    }

    fun clearMessage() { _message.value = "" }
}
