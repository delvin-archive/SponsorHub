package com.example.sponsorhub.data.repository

import com.example.sponsorhub.core.network.RetrofitClient
import com.example.sponsorhub.core.network.SupabaseManager // Balikin pakai SupabaseManager sesuai project lu
import com.example.sponsorhub.data.model.SponsorshipRequest
import com.example.sponsorhub.data.model.SponsorshipRequestWithUmkm
import com.example.sponsorhub.data.remote.request.CreateSponsorshipRequest
import com.example.sponsorhub.data.remote.request.UpdateStatusRequest
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SponsorshipRepository {

    private val apiService = RetrofitClient.apiService
    private val supabaseClient = SupabaseManager.client

    // CREATE pakai Retrofit
    suspend fun createSponsorshipRequest(
        eventId: String,
        title: String,
        description: String
    ): Result<Unit> {
        return try {
            val userId = supabaseClient.auth.currentUserOrNull()?.id
                ?: throw Exception("User not found. Silakan login kembali.")

            // Buat body request sesuai dengan yang ada di ApiService lu
            val requestBody = CreateSponsorshipRequest(
                eventId = eventId,
                umkmId = userId,
                title = title,
                description = description,
                status = "menunggu"
            )

            val response = withContext(Dispatchers.IO) {
                apiService.createSponsorshipRequest(requestBody)
            }

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Gagal mengirim via API: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // GET Requests pakai Retrofit
    suspend fun getRequestsByEvent(eventId: String): List<SponsorshipRequest> {
        return try {
            withContext(Dispatchers.IO) {
                apiService.getRequestsByEvent("eq.$eventId")
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // GET Requests with UMKM pakai Retrofit
    suspend fun getRequestsWithUmkmByEvent(eventId: String): List<SponsorshipRequestWithUmkm> {
        return try {
            val requests = withContext(Dispatchers.IO) {
                apiService.getRequestsByEvent("eq.$eventId")
            }

            requests.map { request ->
                val umkmList = withContext(Dispatchers.IO) {
                    apiService.getUserById("eq.${request.umkmId}")
                }

                SponsorshipRequestWithUmkm(
                    request = request,
                    umkm = umkmList.firstOrNull() // Ambil user pertama kalau ada
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // GET User Request pakai Retrofit
    suspend fun getUserRequestForEvent(eventId: String): SponsorshipRequest? {
        return try {
            val userId = supabaseClient.auth.currentUserOrNull()?.id ?: return null

            val requests = withContext(Dispatchers.IO) {
                apiService.getUserRequestForEvent("eq.$eventId", "eq.$userId")
            }

            requests.firstOrNull() // Return request pertama yang cocok
        } catch (e: Exception) {
            null
        }
    }

    // UPDATE status murni pakai Retrofit
    suspend fun updateRequestStatus(
        requestId: String,
        status: String
    ): Result<Unit> {
        return try {
            val response = withContext(Dispatchers.IO) {
                apiService.updateRequestStatus(
                    id = "eq.$requestId",
                    body = UpdateStatusRequest(status = status)
                )
            }

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Gagal update status via API: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}