package com.example.sponsorhub.data.repository

import com.example.sponsorhub.core.network.SupabaseManager
import com.example.sponsorhub.data.model.User
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.delay

class AuthRepository {

    private val client = SupabaseManager.client

    suspend fun login(
        email: String,
        password: String
    ): Result<Unit> {

        return try {

            client.auth.signInWith(Email) {

                this.email = email
                this.password = password
            }

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    suspend fun register(
        name: String,
        email: String,
        password: String,
        role: String
    ): Result<Unit> {

        return try {

            client.auth.signUpWith(Email) {

                this.email = email
                this.password = password
            }

            delay(1000)

            val currentUser =
                client.auth.currentUserOrNull()
                    ?: throw Exception("User not found")

            val user = User(

                id = currentUser.id,

                name = name,

                role = role
            )

            client
                .from("users")
                .insert(user)

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    suspend fun getCurrentUser(): User? {

        return try {

            val userId =
                client.auth.currentUserOrNull()?.id
                    ?: return null

            client
                .from("users")
                .select {

                    filter {

                        eq("id", userId)
                    }
                }
                .decodeList<User>()
                .firstOrNull()

        } catch (e: Exception) {

            null
        }
    }

    suspend fun getCurrentUserRole(): String {

        return getCurrentUser()?.role ?: ""
    }

    suspend fun logout() {

        client.auth.signOut()
    }
}