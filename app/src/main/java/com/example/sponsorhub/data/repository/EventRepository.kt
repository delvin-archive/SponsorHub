package com.example.sponsorhub.data.repository

import android.content.Context
import android.net.Uri
import com.example.sponsorhub.core.network.SupabaseManager
import com.example.sponsorhub.core.utils.Constants
import com.example.sponsorhub.data.model.Events
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage

class EventRepository {

    private val client =
        SupabaseManager.client

    suspend fun getEventsByRole(
        role: String
    ): List<Events> {

        return try {

            val userId =
                client.auth.currentUserOrNull()?.id
                    ?: return emptyList()

            if (role == "panitia") {

                client
                    .from("events")
                    .select {
                        filter {
                            eq(
                                "created_by",
                                userId
                            )
                        }
                    }
                    .decodeList<Events>()

            } else {

                client
                    .from("events")
                    .select()
                    .decodeList<Events>()
            }

        } catch (e: Exception) {

            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getEventById(
        eventId: String
    ): Events? {

        return try {

            client
                .from("events")
                .select {
                    filter {
                        eq("id", eventId)
                    }
                }
                .decodeList<Events>()
                .firstOrNull()

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

            val userId =
                client.auth
                    .currentUserOrNull()
                    ?.id
                    ?: throw Exception(
                        "User not found"
                    )

            var imageUrl: String? =
                null

            if (imageUri != null) {

                val bytes =
                    context.contentResolver
                        .openInputStream(
                            imageUri
                        )
                        ?.readBytes()

                if (bytes != null) {

                    val fileName =
                        "${System.currentTimeMillis()}.jpg"

                    client.storage
                        .from(
                            Constants.EVENT_BUCKET
                        )
                        .upload(
                            path = fileName,
                            data = bytes
                        ) {
                            upsert = true
                        }

                    imageUrl =
                        client.storage
                            .from(
                                Constants.EVENT_BUCKET
                            )
                            .publicUrl(
                                fileName
                            )
                }
            }

            val event =
                Events(
                    title = title,
                    description =
                        description,
                    location =
                        location,
                    date = date,
                    posterUrl =
                        imageUrl,
                    createdBy =
                        userId
                )

            client
                .from("events")
                .insert(event)

            Result.success(Unit)

        } catch (e: Exception) {

            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun deleteEvent(
        eventId: String
    ): Result<Unit> {

        return try {

            client
                .from("events")
                .delete {
                    filter {
                        eq(
                            "id",
                            eventId
                        )
                    }
                }

            Result.success(Unit)

        } catch (e: Exception) {

            e.printStackTrace()
            Result.failure(e)
        }
    }
}