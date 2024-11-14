package com.android.bangkok2024.example

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.android.bangkok2024.example.firebase.FirebaseActivity
import com.android.bangkok2024.example.lifecycle.LifecycleAwarenessActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Column {
                        val context = LocalContext.current
                        Button(onClick = {
                            context.startActivity(Intent(context, FirebaseActivity::class.java))
                        }) {
                            Text("Firebase Example")
                        }
                        Button(onClick = {
                            context.startActivity(
                                Intent(
                                    context,
                                    LifecycleAwarenessActivity::class.java
                                )
                            )
                        }) {
                            Text("Lifecycle Example")
                        }
                    }
                }
            }
        }
    }
}