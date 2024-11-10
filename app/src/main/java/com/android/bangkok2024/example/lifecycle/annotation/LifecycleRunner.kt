package com.android.bangkok2024.example.lifecycle.annotation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import java.lang.reflect.Method

object LifecycleRunner {

    private val annotations = hashSetOf(
        CreatedToDestroy::class.java,
        StartedToStopped::class.java,
        ResumedToPaused::class.java
    )

    fun extract(target: Any): Map<Annotation, List<Method>> {
        val methodHashMap = hashMapOf<Annotation, ArrayList<Method>>()
        target.javaClass.declaredMethods.asSequence()
            .filter { method ->
                method.returnType.isAssignableFrom(Flow::class.java) &&
                        method.annotations.asSequence().any { annotation ->
                            annotations.any { annotation.annotationClass.java.isAssignableFrom(it) }
                        }
            }
            .distinctBy { it }
            .forEach { method ->

                val annotation = method.annotations.asSequence().first { annotation ->
                    annotations.any { annotation.annotationClass.java.isAssignableFrom(it) }
                }

                methodHashMap.getOrPut(annotation) { arrayListOf() }.add(method)
            }

        return methodHashMap
    }
}

@Composable
fun Any.observeLifecycle() {
    val lifecycleOwner = LocalLifecycleOwner.current
    val target = this
    LaunchedEffect(target, lifecycleOwner) {
        val methods = LifecycleRunner.extract(target)
        methods.entries.forEach { (annotation, methods) ->
            when (annotation) {
                is CreatedToDestroy -> {
                    lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                        methods.map { it.invoke(target) }
                            .filterIsInstance(Flow::class.java)
                            .forEach { async { it.collect() } }
                    }
                }
                is StartedToStopped -> {
                    lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        methods.map { it.invoke(target) }
                            .filterIsInstance(Flow::class.java)
                            .forEach { async { it.collect() } }
                    }
                }
                is ResumedToPaused -> {
                    lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                        methods.map { it.invoke(target) }
                            .filterIsInstance(Flow::class.java)
                            .forEach { async { it.collect() } }
                    }
                }
            }
        }
    }
}