package com.android.bangkok2024.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.android.bangkok2024.example.annotation.FirebaseDatabaseFactory

class HomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repo = DataRepoImpl()
        val repo2 = FirebaseDatabaseFactory.create(DataRepo::class.java)
        setContent {
            MaterialTheme {
                HomeScreen(repo, repo2)
            }
        }
    }
}