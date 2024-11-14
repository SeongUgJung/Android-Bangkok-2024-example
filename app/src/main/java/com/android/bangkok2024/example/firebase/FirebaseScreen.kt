@file:JvmName("HomeScreen")

package com.android.bangkok2024.example.firebase

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.time.Duration.Companion.days

@Composable
internal fun FirebaseScreen(oldClassRepo: DataRepo, retrofitishRepo: DataRepo) {
    Row(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {

            val profile = remember { mutableStateOf(Profile()) }
            LaunchedEffect(oldClassRepo) {
                oldClassRepo.profile().map { it ?: Profile() }.collect {
                    profile.value = it
                }
            }

            val updateProfile = remember { mutableStateOf<Profile?>(null) }
            LaunchedEffect(true) {
                snapshotFlow { updateProfile.value }
                    .filterNotNull()
                    .flatMapLatest { oldClassRepo.setProfile(it) }
                    .stateIn(this)
            }

            Text("Name : ${profile.value.name}")
            Text("Age : ${profile.value.age}")
            Text("Home Addr : ${profile.value.address.home}")
            Text("Office Addr : ${profile.value.address.office}")
            Text("Home Phone : ${profile.value.contact.home}")
            Text("Cell Phone : ${profile.value.contact.cell}")
            Spacer(modifier = Modifier.height(40.dp))
            Button(onClick = {
                val days = System.currentTimeMillis() % 1.days.inWholeMilliseconds
                updateProfile.value = Profile(
                    "test-$days",
                    days.toInt(),
                    Address("home-$days", "office-$days"),
                    Contact("home-$days", "cell-$days")
                )
            }) {
                Text("Update Profile by Impl")
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {

            val profile = remember { mutableStateOf(Profile()) }
            LaunchedEffect(retrofitishRepo) {
                retrofitishRepo.profile().map { it ?: Profile() }.collect {
                    profile.value = it
                }
            }

            val updateProfile = remember { mutableStateOf<Profile?>(null) }
            LaunchedEffect(true) {
                snapshotFlow { updateProfile.value }
                    .filterNotNull()
                    .flatMapLatest { retrofitishRepo.setProfile(it) }
                    .stateIn(this)
            }


            Text("Name : ${profile.value.name}")
            Text("Age : ${profile.value.age}")
            Text("Home Addr : ${profile.value.address.home}")
            Text("Office Addr : ${profile.value.address.office}")
            Text("Home Phone : ${profile.value.contact.home}")
            Text("Cell Phone : ${profile.value.contact.cell}")
            Spacer(modifier = Modifier.height(40.dp))
            Button(onClick = {
                val days = System.currentTimeMillis() % 1.days.inWholeMilliseconds
                updateProfile.value = Profile(
                    "test-$days",
                    days.toInt(),
                    Address("home-$days", "office-$days"),
                    Contact("home-$days", "cell-$days")
                )
            }) {
                Text("Update Profile by annotation")
            }
        }

    }
}
