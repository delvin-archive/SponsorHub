package com.example.sponsorhub.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(

    val id: String = "",

    val name: String = "",

    val role: String = "",

    @SerialName("profile_image")
    val profileImage: String? = null
)