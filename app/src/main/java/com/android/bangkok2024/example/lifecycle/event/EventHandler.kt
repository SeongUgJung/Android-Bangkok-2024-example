package com.android.bangkok2024.example.lifecycle.event

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

interface EventHandler<in Event> {
    fun notify(event: Event): Boolean
    fun <R : Event> observe(eventType: Class<R>): Flow<R>

    companion object {
        inline fun <reified T : Any> EventHandler<T>.event() =
            observe(T::class.java)
    }
}

class EventHandlerImpl<Event> : EventHandler<Event> {
    private val eventFlow = MutableSharedFlow<Event>(extraBufferCapacity = 1)
    override fun notify(event: Event) = eventFlow.tryEmit(event)
    override fun <R : Event> observe(eventType: Class<R>): Flow<R> = eventFlow
        .filter { it!!::class.java.isAssignableFrom(eventType) }
        .map { it as R }
}
