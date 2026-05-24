package com.example.sponsorhub.data.repository

import com.example.sponsorhub.core.network.RetrofitClient
import com.example.sponsorhub.core.network.SupabaseManager
import com.example.sponsorhub.data.model.User
import com.example.sponsorhub.data.remote.request.CreateUserRequest
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.delay

class AuthRepository {

    private val supabaseClient = SupabaseManager.client
    private val apiService = RetrofitClient.apiService

    suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            supabaseClient.auth.signInWith(Email) {
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
            supabaseClient.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }

            delay(1000)

            val currentUser = supabaseClient.auth.currentUserOrNull()
                ?: throw Exception("User not found")

            val response = apiService.createUser(
                CreateUserRequest(
                    id = currentUser.id,
                    name = name,
                    role = role
                )
            )

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Register failed: ${response.code()} ${response.message()}"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCurrentUser(): User? {
        return try {
            val userId = supabaseClient.auth.currentUserOrNull()?.id
                ?: return null

            apiService.getUserById("eq.$userId").firstOrNull()

        } catch (e: Exception) {
            null
        }
    }

    suspend fun getCurrentUserRole(): String {
        return getCurrentUser()?.role ?: ""
    }

    suspend fun logout() {
        supabaseClient.auth.signOut()
    }
}