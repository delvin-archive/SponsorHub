package com.example.sponsorhub.data.repository

import com.example.sponsorhub.core.network.SupabaseManager
import com.example.sponsorhub.data.model.Review
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock.System.now
import kotlin.time.ExperimentalTime

class ReviewRepository {

    private val client = SupabaseManager.client

    @OptIn(ExperimentalTime::class)
    suspend fun createReview(
        sponsorshipId: String,
        reviewStar: Int,
        reviewText: String
    ): Result<Unit> {

        return try {

            val userId =
                client.auth.currentUserOrNull()?.id
                    ?: throw Exception("User not found")

            val existingReview =
                getReviewBySponsorship(
                    sponsorshipId
                )

            if (existingReview != null) {

                return Result.failure(
                    Exception("Review already exists")
                )
            }

            val review = Review(

                userId = userId,

                sponsorshipId = sponsorshipId,

                reviewStar = reviewStar,

                review = reviewText,

                createdAt =
                    now().toLocalDateTime(TimeZone.currentSystemDefault()).toString()
            )

            client
                .from("reviews")
                .insert(review)

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    suspend fun getReviewBySponsorship(
        sponsorshipId: String
    ): Review? {

        return try {

            client
                .from("reviews")
                .select {

                    filter {

                        eq(
                            "sponsorship_id",
                            sponsorshipId
                        )
                    }
                }
                .decodeList<Review>()
                .firstOrNull()

        } catch (e: Exception) {

            null
        }
    }
}