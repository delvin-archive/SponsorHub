package com.example.sponsorhub.data.repository

import com.example.sponsorhub.core.network.SupabaseManager
import com.example.sponsorhub.data.model.SponsorshipRequest
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from

class SponsorshipRepository {

    private val client = SupabaseManager.client

    suspend fun createSponsorshipRequest(
        eventId: String,
        title: String,
        description: String
    ): Result<Unit> {

        return try {

            val userId =
                client.auth.currentUserOrNull()?.id
                    ?: throw Exception("User not found")

            val request = SponsorshipRequest(

                eventId = eventId,

                umkmId = userId,

                title = title,

                description = description,

                status = "menunggu"
            )

            client
                .from("sponsorship_requests")
                .insert(request)

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    suspend fun getRequestsByEvent(
        eventId: String
    ): List<SponsorshipRequest> {

        return try {

            client
                .from("sponsorship_requests")
                .select {

                    filter {

                        eq("event_id", eventId)
                    }
                }
                .decodeList<SponsorshipRequest>()

        } catch (e: Exception) {

            emptyList()
        }
    }

    suspend fun getUserRequestForEvent(
        eventId: String
    ): SponsorshipRequest? {

        return try {

            val userId =
                client.auth.currentUserOrNull()?.id
                    ?: return null

            client
                .from("sponsorship_requests")
                .select {

                    filter {

                        eq("event_id", eventId)
                        eq("umkm_id", userId)
                    }
                }
                .decodeList<SponsorshipRequest>()
                .firstOrNull()

        } catch (e: Exception) {

            null
        }
    }

    suspend fun updateRequestStatus(
        requestId: String,
        status: String
    ): Result<Unit> {

        return try {

            client
                .from("sponsorship_requests")
                .update(
                    {
                        set("status", status)
                    }
                ) {

                    filter {

                        eq("id", requestId)
                    }
                }

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}