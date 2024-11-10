package com.android.bangkok2024.example.lifecycle.annotation

import com.android.bangkok2024.example.lifecycle.LifecycleAwarenessViewModel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LifecycleRunnerTest {

    @Test
    fun extract() {
        val target = LifecycleAwarenessViewModel()

        val methods = LifecycleRunner.extract(target)
        assertEquals(1, methods.size)
    }
}