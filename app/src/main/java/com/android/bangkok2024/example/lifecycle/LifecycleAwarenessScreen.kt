package com.android.bangkok2024.example.lifecycle

import android.icu.text.DecimalFormat
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.android.bangkok2024.example.lifecycle.annotation.observeLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlin.time.Duration.Companion.milliseconds

object LifecycleAwarenessScreen {
    sealed interface Event {
        object ClickReset : Event
    }

    private val decimalFormat = DecimalFormat("00")

    @Composable
    fun Ui(vm: LifecycleAwarenessViewModel) {
        vm.observeLifecycle()
        val time = vm.stopWatch.collectAsState(0L)
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column {
                Timer(time)
                ResetButton { vm.notify(it) }
            }
        }
    }

    @Composable
    private fun Timer(time: State<Long>) {
        val timeValue = remember { mutableStateOf("") }
        LaunchedEffect(time) {
            snapshotFlow { time.value }
                .onEach { timeValue.value = "00:00" }
                .flowOn(Dispatchers.Main)
                .filter { it > 0L }
                .flatMapLatest { original ->
                    flow<Long> {
                        while (true) {
                            emit(System.currentTimeMillis() - original)
                            delay(5L)
                        }
                    }
                        .map { mills ->
                            val milliseconds = mills.milliseconds
                            """${decimalFormat.format(milliseconds.inWholeMinutes)}:${
                                decimalFormat.format(milliseconds.inWholeSeconds)
                            }.${decimalFormat.format(milliseconds.inWholeMilliseconds / 10)}"""
                        }
                        .flowOn(Dispatchers.Default)
                        .onEach { timeValue.value = it }
                        .flowOn(Dispatchers.Main)
                }.collect()
        }
        Text(text = timeValue.value)
    }

    @Composable
    private fun ResetButton(eventFire: (Event) -> Unit) {
        Button(onClick = { eventFire.invoke(Event.ClickReset) }) {
            Text("Reset")
        }
    }
}