package com.android.bangkok2024.example.firebase

import com.android.bangkok2024.example.BuildConfig
import com.android.bangkok2024.example.firebase.annotation.Child
import com.android.bangkok2024.example.firebase.annotation.Data
import com.android.bangkok2024.example.firebase.annotation.FirebaseDatabase
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.skydoves.firebase.database.ktx.flow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

@FirebaseDatabase(
    path = BuildConfig.FIREBASE_DATABASE_URL
)
interface DataRepo {
    @Child("profile")
    fun profile(): Flow<Profile?>

    @Child("profile")
    fun setProfile(@Data profile: Profile): Flow<Boolean>
}

class DataRepoImpl(
    private val database: DatabaseReference = Firebase.database(BuildConfig.FIREBASE_DATABASE_URL).reference
) : DataRepo {
    override fun profile() = database.flow<Profile>(
        path = { it.child("profile") },
        { Json.decodeFromString(it) }
    ).map { it.getOrNull() }

    override fun setProfile(profile: Profile): Flow<Boolean> = callbackFlow {
        database.child("profile")
            .setValue(profile)
            .addOnSuccessListener {
                trySend(true)
                close()
            }
            .addOnFailureListener {
                trySend(false)
                close()
            }
        awaitClose()
    }
}