package com.android.bangkok2024.example.firebase.annotation

import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.skydoves.firebase.database.ktx.flow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Proxy

internal object FirebaseDatabaseFactory {
    @OptIn(ExperimentalStdlibApi::class)
    fun <T> create(clazz: Class<T>): T {
        val annotation = clazz.getAnnotation(FirebaseDatabase::class.java)
        val database: DatabaseReference = Firebase.database(annotation.path).reference
        return Proxy.newProxyInstance(
            this::class.java.classLoader,
            arrayOf(clazz)
        ) { proxy, method, args ->
            return@newProxyInstance when (method.name) {
                "equals" -> proxy === args[0]
                "hashCode" -> System.identityHashCode(proxy)
                "toString" -> "${proxy.javaClass.getName()}@${
                    System.identityHashCode(proxy).toHexString()
                }"
                else -> invoker(database, method, args)
            }

        } as T
    }

    private fun invoker(database: DatabaseReference, method: Method, args: Array<Any>?): Any {

        if (method.returnType != Flow::class.java) {
            throw IllegalStateException("we only accept Flow<T>")
        }

        val childAnnotation = method.getAnnotation(Child::class.java)

        val dataParamIndex = if (args?.isNotEmpty() != null) {
            method.parameterAnnotations.indexOfFirst { it.any { it is Data } }
        } else {
            -1
        }

        return if (dataParamIndex < 0) {
            // read
            database.flow(
                { it.child(childAnnotation.path) },
                {
                    Json.decodeFromString(
                        serializer((method.genericReturnType as ParameterizedType).actualTypeArguments[0]),
                        it
                    )
                })
                .map { it.getOrNull() }
        } else {
            callbackFlow {

                database.child("profile")
                    .setValue(args!!.get(0))
                    .addOnSuccessListener {
                        trySend(true)
                        close()
                    }
                    .addOnFailureListener {
                        trySend(false)
                        close()
                    }
                awaitClose()
            }
        }
    }
}