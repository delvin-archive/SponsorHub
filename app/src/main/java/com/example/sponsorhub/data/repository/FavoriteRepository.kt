package com.example.sponsorhub.data.repository

import com.example.sponsorhub.core.network.RetrofitClient
import com.example.sponsorhub.core.network.SupabaseManager
import com.example.sponsorhub.data.remote.request.CreateFavoriteRequest
import io.github.jan.supabase.auth.auth

class FavoriteRepository {

    private val apiService = RetrofitClient.apiService
    private val supabaseClient = SupabaseManager.client

    suspend fun addFavorite(umkmId: String): Result<Unit> {
        return try {
            val panitiaId = supabaseClient.auth.currentUserOrNull()?.id
                ?: throw Exception("User tidak ditemukan")

            val response = apiService.addFavorite(
                CreateFavoriteRequest(panitiaId = panitiaId, umkmId = umkmId)
            )

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Gagal menambahkan favorit: ${response.code()}"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getFavoriteCount(umkmId: String): Int {
        return try {
            apiService.getFavoritesByUmkm("eq.$umkmId").size
        } catch (e: Exception) {
            0
        }
    }

    suspend fun isAlreadyFavorited(umkmId: String): Boolean {
        return try {
            val panitiaId = supabaseClient.auth.currentUserOrNull()?.id
                ?: return false

            apiService.checkFavorite(
                panitiaId = "eq.$panitiaId",
                umkmId = "eq.$umkmId"
            ).isNotEmpty()

        } catch (e: Exception) {
            false
        }
    }
}
