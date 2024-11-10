package com.android.bangkok2024.example.lifecycle

import com.android.bangkok2024.example.lifecycle.LifecycleAwarenessScreen.Event
import com.android.bangkok2024.example.lifecycle.LifecycleAwarenessScreen.Event.ClickReset
import com.android.bangkok2024.example.lifecycle.annotation.ResumedToPaused
import com.android.bangkok2024.example.lifecycle.event.EventHandler
import com.android.bangkok2024.example.lifecycle.event.EventHandler.Companion.event
import com.android.bangkok2024.example.lifecycle.event.EventHandlerImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

class LifecycleAwarenessViewModel : EventHandler<Event> by EventHandlerImpl() {
    val stopWatch = MutableStateFlow(0L)

    @ResumedToPaused
    fun onReset() = event<ClickReset>().map { true }
        .onStart { emit(true) }
        .onEach {
            stopWatch.tryEmit(System.currentTimeMillis())
        }

}