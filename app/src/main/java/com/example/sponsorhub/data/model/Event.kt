package com.example.sponsorhub.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Event(

    @SerialName("id")
    val id: String = "",

    @SerialName("title")
    val title: String = "",

    @SerialName("description")
    val description: String = "",

    @SerialName("date")
    val date: String = "",

    @SerialName("location")
    val location: String = "",

    @SerialName("poster_url")
    val posterUrl: String? = null,

    @SerialName("created_by")
    val createdBy: String = ""
)