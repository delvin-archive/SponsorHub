package com.example.sponsorhub.feature.profile

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sponsorhub.core.network.SupabaseManager
import com.example.sponsorhub.data.model.User
import com.example.sponsorhub.data.repository.AuthRepository
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val client = SupabaseManager.client

    private val authRepository =
        AuthRepository()

    private val _user =
        MutableStateFlow<User?>(null)

    val user =
        _user.asStateFlow()

    fun loadProfile() {

        viewModelScope.launch {

            try {

                val userId =
                    client.auth.currentUserOrNull()?.id
                        ?: return@launch

                val result =
                    client
                        .from("users")
                        .select {

                            filter {

                                eq("id", userId)
                            }
                        }
                        .decodeList<User>()
                        .firstOrNull()

                _user.value = result

            } catch (_: Exception) {

            }
        }
    }

    fun uploadProfileImage(
        context: Context,
        imageUri: Uri
    ) {

        viewModelScope.launch {

            try {

                val userId =
                    client.auth.currentUserOrNull()?.id
                        ?: return@launch

                val bytes =
                    context.contentResolver
                        .openInputStream(imageUri)
                        ?.readBytes()
                        ?: return@launch

                val fileName =
                    "$userId.jpg"

                val bucket =
                    client.storage
                        .from("profile_images")

                bucket.upload(

                    path = fileName,

                    data = bytes

                ) {

                    upsert = true
                }

                val imageUrl =
                    bucket.publicUrl(fileName)

                client
                    .from("users")
                    .update(
                        {
                            set(
                                "profile_image",
                                imageUrl
                            )
                        }
                    ) {

                        filter {

                            eq("id", userId)
                        }
                    }

                loadProfile()

            } catch (_: Exception) {

            }
        }
    }

    fun logout(
        onSuccess: () -> Unit
    ) {

        viewModelScope.launch {

            authRepository.logout()

            onSuccess()
        }
    }
}