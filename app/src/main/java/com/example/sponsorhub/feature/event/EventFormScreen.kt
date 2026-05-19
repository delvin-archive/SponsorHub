package com.example.sponsorhub.feature.event

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.sponsorhub.core.network.SupabaseManager
import com.example.sponsorhub.data.model.Event
import com.example.sponsorhub.data.repository.EventRepository
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch

@Composable
fun EventFormScreen(
    navController: NavHostController,
    eventId: String? = null
) {

    val repository = remember {

        EventRepository()
    }

    val client = SupabaseManager.client

    val scope = rememberCoroutineScope()

    var title by remember {

        mutableStateOf("")
    }

    var description by remember {

        mutableStateOf("")
    }

    var location by remember {

        mutableStateOf("")
    }

    var date by remember {

        mutableStateOf("")
    }

    var contactNumber by remember {

        mutableStateOf("")
    }

    Column(

        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(24.dp)
    ) {

        Text(

            text =
                if (eventId == null)
                    "Buat Event"
                else
                    "Edit Event",

            style =
                MaterialTheme
                    .typography
                    .headlineMedium
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        OutlinedTextField(

            value = title,

            onValueChange = {

                title = it
            },

            label = {

                Text("Judul Event")
            },

            modifier =
                Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedTextField(

            value = description,

            onValueChange = {

                description = it
            },

            label = {

                Text("Deskripsi")
            },

            minLines = 4,

            modifier =
                Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedTextField(

            value = location,

            onValueChange = {

                location = it
            },

            label = {

                Text("Lokasi")
            },

            modifier =
                Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedTextField(

            value = date,

            onValueChange = {

                date = it
            },

            label = {

                Text("Tanggal")
            },

            modifier =
                Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedTextField(

            value = contactNumber,

            onValueChange = {

                contactNumber = it
            },

            label = {

                Text("Nomor Hubung Panitia")
            },

            modifier =
                Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Button(

            onClick = {

                scope.launch {

                    val userId =
                        client.auth
                            .currentUserOrNull()
                            ?.id
                            ?: return@launch

                    val event = Event(

                        id =
                            eventId ?: "",

                        title = title,

                        description =
                            description,

                        location = location,

                        date = date,

                        createdBy = userId,

                        contactNumber = contactNumber
                    )

                    val result =

                        if (eventId == null) {

                            repository
                                .createEvent(
                                    event
                                )

                        } else {

                            repository
                                .updateEvent(
                                    event
                                )
                        }

                    if (result.isSuccess) {

                        navController
                            .popBackStack()
                    }
                }
            },

            modifier =
                Modifier.fillMaxWidth()
        ) {

            Text("Simpan")
        }
    }
}