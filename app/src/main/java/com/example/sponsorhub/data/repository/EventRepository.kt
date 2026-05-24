package com.example.sponsorhub.data.repository

import android.content.Context
import android.net.Uri
import com.example.sponsorhub.core.network.RetrofitClient
import com.example.sponsorhub.core.network.SupabaseManager
import com.example.sponsorhub.core.utils.Constants
import com.example.sponsorhub.data.model.Events
import com.example.sponsorhub.data.remote.request.CreateEventRequest
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.storage.storage

class EventRepository {

    private val apiService = RetrofitClient.apiService
    private val supabaseClient = SupabaseManager.client

    suspend fun getEventsByRole(role: String): List<Events> {
        return try {
            val userId = supabaseClient.auth.currentUserOrNull()?.id
                ?: return emptyList()

            if (role == "panitia") {
                apiService.getEventsByCreator("eq.$userId")
            } else {
                apiService.getEvents()
            }

        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getEventById(eventId: String): Events? {
        return try {
            apiService.getEventById("eq.$eventId").firstOrNull()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun createEvent(
        context: Context,
        title: String,
        description: String,
        location: String,
        date: String,
        imageUri: Uri?
    ): Result<Unit> {
        return try {
            val userId = supabaseClient.auth.currentUserOrNull()?.id
                ?: throw Exception("User not found")

            var imageUrl: String? = null

            if (imageUri != null) {
                val bytes = context.contentResolver
                    .openInputStream(imageUri)
                    ?.readBytes()

                if (bytes != null) {
                    val fileName = "${System.currentTimeMillis()}.jpg"

                    supabaseClient.storage
                        .from(Constants.EVENT_BUCKET)
                        .upload(path = fileName, data = bytes) {
                            upsert = true
                        }

                    imageUrl = supabaseClient.storage
                        .from(Constants.EVENT_BUCKET)
                        .publicUrl(fileName)
                }
            }

            val response = apiService.createEvent(
                CreateEventRequest(
                    title = title,
                    description = description,
                    location = location,
                    date = date,
                    posterUrl = imageUrl,
                    createdBy = userId
                )
            )

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Create event failed: ${response.code()} ${response.message()}"))
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun deleteEvent(eventId: String): Result<Unit> {
        return try {
            val response = apiService.deleteEvent("eq.$eventId")

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Delete event failed: ${response.code()} ${response.message()}"))
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}