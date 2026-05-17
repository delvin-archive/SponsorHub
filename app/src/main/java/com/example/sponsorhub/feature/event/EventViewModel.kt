package com.example.sponsorhub.feature.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sponsorhub.data.model.Event
import com.example.sponsorhub.data.model.SponsorshipRequest
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
        MutableStateFlow<List<Event>>(emptyList())

    val events =
        _events.asStateFlow()

    private val _event =
        MutableStateFlow<Event?>(null)

    val event =
        _event.asStateFlow()

    private val _role =
        MutableStateFlow("")

    val role =
        _role.asStateFlow()

    private val _requests =
        MutableStateFlow<List<SponsorshipRequest>>(
            emptyList()
        )

    val requests =
        _requests.asStateFlow()

    private val _userRequest =
        MutableStateFlow<SponsorshipRequest?>(null)

    val userRequest =
        _userRequest.asStateFlow()

    fun loadEvents() {

        viewModelScope.launch {

            val role =
                authRepository.getCurrentUserRole()

            _role.value = role

            _events.value =
                eventRepository.getEventsByRole(
                    role
                )
        }
    }

    fun loadEventDetail(
        eventId: String
    ) {

        viewModelScope.launch {

            _event.value =
                eventRepository.getEventById(
                    eventId
                )

            val role =
                authRepository.getCurrentUserRole()

            _role.value = role

            if (role == "panitia") {

                _requests.value =
                    sponsorshipRepository
                        .getRequestsByEvent(
                            eventId
                        )

            } else {

                _userRequest.value =
                    sponsorshipRepository
                        .getUserRequestForEvent(
                            eventId
                        )
            }
        }
    }

    fun deleteEvent(
        eventId: String,
        onSuccess: () -> Unit
    ) {

        viewModelScope.launch {

            val result =
                eventRepository.deleteEvent(
                    eventId
                )

            if (result.isSuccess) {

                onSuccess()
            }
        }
    }

    fun updateRequestStatus(
        requestId: String,
        status: String
    ) {

        viewModelScope.launch {

            sponsorshipRepository
                .updateRequestStatus(
                    requestId,
                    status
                )

            _event.value?.id?.let {

                loadEventDetail(it)
            }
        }
    }
}