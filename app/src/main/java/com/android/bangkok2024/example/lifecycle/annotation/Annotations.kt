package com.android.bangkok2024.example.lifecycle.annotation

@Retention(AnnotationRetention.RUNTIME)
annotation class CreatedToDestroy

@Retention(AnnotationRetention.RUNTIME)
annotation class StartedToStopped

@Retention(AnnotationRetention.RUNTIME)
annotation class ResumedToPaused