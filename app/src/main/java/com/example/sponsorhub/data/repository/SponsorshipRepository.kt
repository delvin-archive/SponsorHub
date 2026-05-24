package com.example.sponsorhub.data.repository

import com.example.sponsorhub.core.network.RetrofitClient
import com.example.sponsorhub.core.network.SupabaseManager
import com.example.sponsorhub.data.model.SponsorshipRequest
import com.example.sponsorhub.data.model.SponsorshipRequestWithUmkm
import com.example.sponsorhub.data.model.User
import io.github.jan.supabase.auth.auth

class SponsorshipRepository {

    private val apiService = RetrofitClient.apiService
    private val supabaseClient = SupabaseManager.client

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

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Create sponsorship failed: ${response.code()} ${response.message()}"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRequestsByEvent(eventId: String): List<SponsorshipRequest> {
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

    suspend fun getRequestsWithUmkmByEvent(
        eventId: String
    ): List<SponsorshipRequestWithUmkm> {

        return try {

            val requests = client
                .from("sponsorship_requests")
                .select {
                    filter {
                        eq("event_id", eventId)
                    }
                }
                .decodeList<SponsorshipRequest>()

            requests.map { request ->

                val umkm = client
                    .from("users")
                    .select {
                        filter {
                            eq("id", request.umkmId)
                        }
                    }
                    .decodeList<User>()
                    .firstOrNull()

                SponsorshipRequestWithUmkm(
                    request = request,
                    umkm = umkm
                )
            }

        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getUserRequestForEvent(eventId: String): SponsorshipRequest? {
        return try {
            val userId = supabaseClient.auth.currentUserOrNull()?.id
                ?: return null

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
            val response = apiService.updateRequestStatus(
                id = "eq.$requestId",
                body = UpdateStatusRequest(status = status)
            )

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