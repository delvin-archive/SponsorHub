package com.example.sponsorhub.data.repository

import com.example.sponsorhub.core.network.RetrofitClient
import com.example.sponsorhub.core.network.SupabaseManager
import com.example.sponsorhub.data.model.SponsorshipRequest
import com.example.sponsorhub.data.remote.request.CreateSponsorshipRequest
import com.example.sponsorhub.data.remote.request.UpdateStatusRequest
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
            val userId = supabaseClient.auth.currentUserOrNull()?.id
                ?: throw Exception("User not found")

            val response = apiService.createSponsorshipRequest(
                CreateSponsorshipRequest(
                    eventId = eventId,
                    umkmId = userId,
                    title = title,
                    description = description,
                    status = "menunggu"
                )
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
            apiService.getRequestsByEvent("eq.$eventId")
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getUserRequestForEvent(eventId: String): SponsorshipRequest? {
        return try {
            val userId = supabaseClient.auth.currentUserOrNull()?.id
                ?: return null

            apiService.getUserRequestForEvent(
                eventId = "eq.$eventId",
                umkmId = "eq.$userId"
            ).firstOrNull()

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

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Update status failed: ${response.code()} ${response.message()}"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}