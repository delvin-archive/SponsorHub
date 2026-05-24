package com.example.sponsorhub.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SponsorshipRequest(

    val id: String = "",

    @SerialName("event_id")
    val eventId: String = "",

    @SerialName("umkm_id")
    val umkmId: String = "",

    val title: String = "",

    val description: String = "",

    val status: String = "menunggu"
)