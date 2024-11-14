package com.android.bangkok2024.example.firebase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.android.bangkok2024.example.firebase.annotation.FirebaseDatabaseFactory

class FirebaseActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repo = DataRepoImpl()
        val repo2 = FirebaseDatabaseFactory.create(DataRepo::class.java)
        setContent {
            MaterialTheme {
                FirebaseScreen(repo, repo2)
            }
        }
    }
}