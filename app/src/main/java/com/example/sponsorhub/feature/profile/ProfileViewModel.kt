package com.example.sponsorhub.feature.profile

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sponsorhub.core.network.RetrofitClient
import com.example.sponsorhub.core.network.SupabaseManager
import com.example.sponsorhub.data.model.User
import com.example.sponsorhub.data.remote.request.UpdateProfileImageRequest
import com.example.sponsorhub.data.repository.AuthRepository
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val supabaseClient = SupabaseManager.client
    private val authRepository = AuthRepository()
    private val apiService = RetrofitClient.apiService

    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    fun loadProfile() {
        viewModelScope.launch {
            try {
                _user.value = authRepository.getCurrentUser()
            } catch (_: Exception) { }
        }
    }

    fun uploadProfileImage(
        context: Context,
        imageUri: Uri
    ) {
        viewModelScope.launch {
            try {
                val userId = supabaseClient.auth.currentUserOrNull()?.id
                    ?: return@launch

                val bytes = context.contentResolver
                    .openInputStream(imageUri)
                    ?.readBytes()
                    ?: return@launch

                val fileName = "$userId.jpg"

                val bucket = supabaseClient.storage.from("profile_images")

                bucket.upload(path = fileName, data = bytes) {
                    upsert = true
                }

                val imageUrl = bucket.publicUrl(fileName)

                // Update profile_image via Retrofit (bukan langsung postgrest)
                apiService.updateProfileImage(
                    id = "eq.$userId",
                    body = UpdateProfileImageRequest(profileImage = imageUrl)
                )

                loadProfile()

            } catch (_: Exception) { }
        }
    }

    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            authRepository.logout()
            onSuccess()
        }
    }
}