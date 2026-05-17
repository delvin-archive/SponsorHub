package com.example.sponsorhub.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Review(

    @SerialName("id")
    val id: String = "",

    @SerialName("user_id")
    val userId: String = "",

    @SerialName("sponsorship_id")
    val sponsorshipId: String = "",

    @SerialName("review_star")
    val reviewStar: Int = 0,

    val review: String = "",

    @SerialName("created_at")
    val createdAt: String = ""
)