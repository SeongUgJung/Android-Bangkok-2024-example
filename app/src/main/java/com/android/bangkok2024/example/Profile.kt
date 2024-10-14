package com.android.bangkok2024.example

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val name: String = "",
    val age: Int = -1,
    val address: Address = Address(),
    val contact: Contact = Contact(),
)

@Serializable
data class Address(
    val home: String = "",
    val office: String = "",
)

@Serializable
data class Contact(
    val home: String = "",
    val cell: String = "",
)