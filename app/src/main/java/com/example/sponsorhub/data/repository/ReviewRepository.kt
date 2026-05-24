package com.example.sponsorhub.data.repository

import com.example.sponsorhub.core.network.RetrofitClient
import com.example.sponsorhub.core.network.SupabaseManager
import com.example.sponsorhub.data.model.Review
import com.example.sponsorhub.data.remote.request.CreateReviewRequest
import io.github.jan.supabase.auth.auth
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock.System.now
import kotlin.time.ExperimentalTime

class ReviewRepository {

    private val apiService = RetrofitClient.apiService
    private val supabaseClient = SupabaseManager.client

    @OptIn(ExperimentalTime::class)
    suspend fun createReview(
        sponsorshipId: String,
        reviewStar: Int,
        reviewText: String
    ): Result<Unit> {
        return try {
            val userId = supabaseClient.auth.currentUserOrNull()?.id
                ?: throw Exception("User not found")

            val existingReview = getReviewBySponsorship(sponsorshipId)
            if (existingReview != null) {
                return Result.failure(Exception("Review already exists"))
            }

            val createdAt = now()
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .toString()

            val response = apiService.createReview(
                CreateReviewRequest(
                    userId = userId,
                    sponsorshipId = sponsorshipId,
                    reviewStar = reviewStar,
                    review = reviewText,
                    createdAt = createdAt
                )
            )

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Create review failed: ${response.code()} ${response.message()}"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getReviewBySponsorship(sponsorshipId: String): Review? {
        return try {
            apiService.getReviewBySponsorship("eq.$sponsorshipId").firstOrNull()
        } catch (e: Exception) {
            null
        }
    }
}