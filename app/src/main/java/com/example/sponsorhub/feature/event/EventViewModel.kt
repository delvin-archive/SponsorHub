package com.example.sponsorhub.feature.event

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sponsorhub.data.model.Events
import com.example.sponsorhub.data.repository.AuthRepository
import com.example.sponsorhub.data.repository.EventRepository
import com.example.sponsorhub.data.repository.SponsorshipRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EventViewModel : ViewModel() {

    private val eventRepository =
        EventRepository()

    private val authRepository =
        AuthRepository()

    private val sponsorshipRepository =
        SponsorshipRepository()

    private val _events =
        MutableStateFlow<List<Events>>(emptyList())

    val events =
        _events.asStateFlow()

    private val _selectedEvent =
        MutableStateFlow<Events?>(null)

    val selectedEvent =
        _selectedEvent.asStateFlow()

    private val _role =
        MutableStateFlow("")

    val role =
        _role.asStateFlow()

    private val _isSuccess =
        MutableStateFlow(false)

    val isSuccess =
        _isSuccess.asStateFlow()

    private val _message =
        MutableStateFlow("")

    val message =
        _message.asStateFlow()

    init {
        loadRole()
    }

    fun loadRole() {

        viewModelScope.launch {

            _role.value =
                authRepository
                    .getCurrentUserRole()
        }
    }

    fun loadEvents() {

        viewModelScope.launch {

            val currentRole =
                authRepository
                    .getCurrentUserRole()

            _role.value =
                currentRole

            _events.value =
                eventRepository
                    .getEventsByRole(
                        currentRole
                    )
        }
    }

    fun loadEventById(
        eventId: String
    ) {

        viewModelScope.launch {

            _selectedEvent.value =
                eventRepository
                    .getEventById(
                        eventId
                    )
        }
    }

    fun createEvent(
        context: Context,
        title: String,
        description: String,
        location: String,
        date: String,
        imageUri: Uri?
    ) {

        viewModelScope.launch {

            val result =
                eventRepository
                    .createEvent(
                        context = context,
                        title = title,
                        description = description,
                        location = location,
                        date = date,
                        imageUri = imageUri
                    )

            result.fold(

                onSuccess = {

                    _isSuccess.value =
                        true

                    _message.value =
                        "Event berhasil dibuat"

                    loadEvents()
                },

                onFailure = {

                    _isSuccess.value =
                        false

                    _message.value =
                        it.message
                            ?: "Gagal membuat event"
                }
            )
        }
    }

    fun deleteEvent(
        eventId: String
    ) {

        viewModelScope.launch {

            val result =
                eventRepository
                    .deleteEvent(
                        eventId
                    )

            result.fold(

                onSuccess = {

                    _message.value =
                        "Event berhasil dihapus"

                    loadEvents()
                },

                onFailure = {

                    _message.value =
                        it.message
                            ?: "Gagal menghapus event"
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