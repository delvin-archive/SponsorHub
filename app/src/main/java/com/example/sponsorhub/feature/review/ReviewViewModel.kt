package com.example.sponsorhub.feature.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sponsorhub.data.repository.ReviewRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReviewViewModel : ViewModel() {

    private val repository =
        ReviewRepository()

    private val _message =
        MutableStateFlow("")

    val message =
        _message.asStateFlow()

    private val _isSuccess =
        MutableStateFlow(false)

    val isSuccess =
        _isSuccess.asStateFlow()

    fun createReview(
        sponsorshipId: String,
        reviewStar: Int,
        review: String
    ) {

        viewModelScope.launch {

            val result =
                repository.createReview(
                    sponsorshipId,
                    reviewStar,
                    review
                )

            if (result.isSuccess) {

                _isSuccess.value = true

                _message.value =
                    "Review berhasil dibuat"

            } else {

                _isSuccess.value = false

                _message.value =
                    result.exceptionOrNull()?.message
                        ?: "Review gagal dibuat"
            }
        }
    }

    fun resetState() {

        _isSuccess.value = false

        _message.value = ""
    }
}