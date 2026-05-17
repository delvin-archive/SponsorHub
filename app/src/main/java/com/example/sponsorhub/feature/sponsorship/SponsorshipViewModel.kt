package com.example.sponsorhub.feature.sponsorship

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sponsorhub.data.repository.SponsorshipRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SponsorshipViewModel : ViewModel() {

    private val repository =
        SponsorshipRepository()

    private val _isSuccess =
        MutableStateFlow(false)

    val isSuccess =
        _isSuccess.asStateFlow()

    private val _message =
        MutableStateFlow("")

    val message =
        _message.asStateFlow()

    fun createSponsorshipRequest(
        eventId: String,
        title: String,
        description: String
    ) {

        viewModelScope.launch {

            val result =
                repository
                    .createSponsorshipRequest(
                        eventId,
                        title,
                        description
                    )

            if (result.isSuccess) {

                _isSuccess.value = true

                _message.value =
                    "Pengajuan berhasil"

            } else {

                _isSuccess.value = false

                _message.value =
                    result.exceptionOrNull()?.message
                        ?: "Pengajuan gagal"
            }
        }
    }

    fun resetState() {

        _isSuccess.value = false

        _message.value = ""
    }
}