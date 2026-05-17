package com.example.sponsorhub.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SponsorshipRequest(

    @SerialName("id")
    val id: String = "",

    @SerialName("umkm_id")
    val umkmId: String = "",

    @SerialName("event_id")
    val eventId: String = "",

    @SerialName("title")
    val title: String = "",

    @SerialName("description")
    val description: String = "",

    @SerialName("proposal_file")
    val proposalFile: String? = null,

    @SerialName("status")
    val status: String = "pending"
)