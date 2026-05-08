package com.mari.appp.ui.supabase

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

object SupabaseClient {

    val client = createSupabaseClient(
        supabaseUrl = "https://lolxposknkbshfbnhjbl.supabase.co/rest/v1/",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImxvbHhwb3Nrbmtic2hmYm5oamJsIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzgyMzQ0NzMsImV4cCI6MjA5MzgxMDQ3M30.g-oVHW7OUHJxep7IzGj0CPPkhrZ0TcnwJpFwo2Py9GU"
    ) {
        install(Postgrest)
        install(Auth)
        install(Storage)
    }
}