package com.example.sponsorhub.core.network

import com.example.sponsorhub.core.utils.Constants
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.storage.Storage

object SupabaseManager {

    val client: SupabaseClient = createSupabaseClient(
        supabaseUrl = Constants.SUPABASE_URL,
        supabaseKey = Constants.SUPABASE_ANON_KEY
    ) {
        install(Auth)
        install(Storage)
        // Postgrest dihapus — semua query DB sekarang via Retrofit (RetrofitClient)
    }
}