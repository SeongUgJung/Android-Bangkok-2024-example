package com.android.bangkok2024.example.lifecycle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.android.bangkok2024.example.lifecycle.LifecycleAwarenessScreen.Ui

class LifecycleAwarenessActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val vm = LifecycleAwarenessViewModel()
        setContent {
            MaterialTheme {
                Ui(vm)
            }
        }
    }
}