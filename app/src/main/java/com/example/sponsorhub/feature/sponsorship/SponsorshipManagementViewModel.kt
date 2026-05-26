package com.example.sponsorhub.feature.sponsorship

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sponsorhub.data.model.SponsorshipRequest
import com.example.sponsorhub.data.repository.SponsorshipRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SponsorshipManagementViewModel : ViewModel() {

    private val sponsorshipRepository = SponsorshipRepository()

    private val _requests = MutableStateFlow<List<SponsorshipRequest>>(emptyList())
    val requests = _requests.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun loadRequestsByEvent(eventId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _requests.value = sponsorshipRepository.getRequestsByEvent(eventId)
            _isLoading.value = false
        }
    }
}
