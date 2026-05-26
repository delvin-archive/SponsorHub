package com.example.sponsorhub.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Favorite(
    @SerialName("id") val id: String = "",
    @SerialName("panitia_id") val panitiaId: String = "",
    @SerialName("umkm_id") val umkmId: String = "",
    @SerialName("created_at") val createdAt: String = ""
)
