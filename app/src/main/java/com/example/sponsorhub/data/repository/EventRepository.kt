package com.example.sponsorhub.data.repository

import com.example.sponsorhub.core.network.SupabaseManager
import com.example.sponsorhub.data.model.Event
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from

class EventRepository {

    private val client = SupabaseManager.client

    suspend fun getEventsByRole(
        role: String
    ): List<Event> {

        return try {

            val userId =
                client.auth.currentUserOrNull()?.id
                    ?: return emptyList()

            if (role == "panitia") {

                client
                    .from("events")
                    .select {

                        filter {

                            eq("created_by", userId)
                        }
                    }
                    .decodeList<Event>()

            } else {

                client
                    .from("events")
                    .select()
                    .decodeList<Event>()
            }

        } catch (e: Exception) {

            emptyList()
        }
    }

    suspend fun getEventById(
        eventId: String
    ): Event? {

        return try {

            client
                .from("events")
                .select {

                    filter {

                        eq("id", eventId)
                    }
                }
                .decodeList<Event>()
                .firstOrNull()

        } catch (e: Exception) {

            null
        }
    }

    suspend fun createEvent(
        event: Event
    ): Result<Unit> {

        return try {

            client
                .from("events")
                .insert(event)

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    suspend fun updateEvent(
        event: Event
    ): Result<Unit> {

        return try {

            client
                .from("events")
                .update(event) {

                    filter {

                        eq("id", event.id)
                    }
                }

            Result.success(Unit)

        } catch (e: Exception) {

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

                        eq("id", eventId)
                    }
                }

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}