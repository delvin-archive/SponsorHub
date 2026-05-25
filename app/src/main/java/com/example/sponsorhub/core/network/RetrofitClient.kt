package com.example.sponsorhub.core.network

import com.example.sponsorhub.core.utils.Constants
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import io.github.jan.supabase.auth.auth
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object RetrofitClient {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    private val authInterceptor = Interceptor { chain ->
        val token = try {
            SupabaseManager.client.auth.currentAccessTokenOrNull()
        } catch (e: Exception) {
            null
        }

        val request = chain.request().newBuilder()
            .addHeader("apikey", Constants.SUPABASE_ANON_KEY)
            .addHeader("Content-Type", "application/json")
            .addHeader("Prefer", "return=representation")
            .apply {
                if (token != null) {
                    addHeader("Authorization", "Bearer $token")
                } else {
                    addHeader("Authorization", "Bearer ${Constants.SUPABASE_ANON_KEY}")
                }
            }
            .build()

        chain.proceed(request)
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("${Constants.SUPABASE_URL}/rest/v1/")
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(ApiService::class.java)
    }
}
