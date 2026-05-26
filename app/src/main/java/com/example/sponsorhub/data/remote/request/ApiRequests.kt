package com.example.sponsorhub.data.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateArticleRequest(
    @SerialName("title") val title: String,
    @SerialName("content") val content: String,
    @SerialName("category") val category: String,
    @SerialName("image_url") val imageUrl: String? = null
)

@Serializable
data class CreateEventRequest(
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("location") val location: String,
    @SerialName("date") val date: String,
    @SerialName("poster_url") val posterUrl: String? = null,
    @SerialName("created_by") val createdBy: String
)

@Serializable
data class CreateProductRequest(
@SerialName("id") val id: String = "",
@SerialName("product_name") val productName: String = "",
@SerialName("description") val description: String = "",
@SerialName("product_url") val productUrl: String? = null,
@SerialName("user_id") val userId: String = ""
)

@Serializable
data class CreateReviewRequest(
    @SerialName("user_id") val userId: String,
    @SerialName("sponsorship_id") val sponsorshipId: String,
    @SerialName("review_star") val reviewStar: Int,
    @SerialName("review") val review: String,
    @SerialName("created_at") val createdAt: String
)

@Serializable
data class CreateSponsorshipRequest(
    @SerialName("event_id") val eventId: String,
    @SerialName("umkm_id") val umkmId: String,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("status") val status: String = "menunggu"
)

@Serializable
data class UpdateStatusRequest(
    @SerialName("status") val status: String
)

@Serializable
data class CreateUserRequest(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("role") val role: String
)

@Serializable
data class UpdateProfileImageRequest(
    @SerialName("profile_image") val profileImage: String
)

@Serializable
data class CreateFavoriteRequest(
    @SerialName("panitia_id") val panitiaId: String,
    @SerialName("umkm_id") val umkmId: String
)
