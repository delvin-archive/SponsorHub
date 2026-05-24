package com.example.sponsorhub.feature.event

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.sponsorhub.navigation.Routes

@Composable
fun EventDetailScreen(
    navController: NavHostController,
    eventId: String,
    viewModel: EventViewModel =
        androidx.lifecycle.viewmodel.compose.viewModel()
) {

    val event by
    viewModel
        .selectedEvent
        .collectAsState()

    val role by
    viewModel
        .role
        .collectAsState()

    val message by
    viewModel
        .message
        .collectAsState()

    LaunchedEffect(Unit) {

        viewModel.loadRole()
        viewModel.loadEventById(
            eventId
        )
    }

    if (event == null) {

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement =
                Arrangement.Center,
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            CircularProgressIndicator()
        }

        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(20.dp)
    ) {

        // EVENT POSTER
        if (
            !event
                ?.posterUrl
                .isNullOrBlank()
        ) {

            AsyncImage(
                model =
                    event?.posterUrl,
                contentDescription =
                    null,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(240.dp)
            )
        }

        Spacer(
            modifier =
                Modifier.height(20.dp)
        )

        // TITLE
        Text(
            text =
                event?.title
                    ?: "",
            style =
                MaterialTheme
                    .typography
                    .headlineMedium
        )

        Spacer(
            modifier =
                Modifier.height(12.dp)
        )

        // LOCATION
        AssistChip(
            onClick = {},
            label = {
                Text(
                    event?.location
                        ?: ""
                )
            }
        )

        Spacer(
            modifier =
                Modifier.height(8.dp)
        )

        // DATE
        Text(
            text =
                event?.date
                    ?: "",
            style =
                MaterialTheme
                    .typography
                    .bodyMedium
        )

        Spacer(
            modifier =
                Modifier.height(20.dp)
        )

        // DESCRIPTION
        Text(
            text =
                event?.description
                    ?: "",
            style =
                MaterialTheme
                    .typography
                    .bodyLarge
        )

        Spacer(
            modifier =
                Modifier.height(32.dp)
        )

        // ROLE-BASED ACTION
        if (role == "umkm") {

            Button(
                onClick = {

                    navController
                        .navigate(
                            "${Routes.SPONSORSHIP_FORM}/$eventId"
                        )
                },
                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Text(
                    "Ajukan Sponsorship"
                )
            }
        }

        if (role == "panitia") {

            Button(
                onClick = {

                    event?.id?.let {

                        viewModel
                            .deleteEvent(
                                it
                            )

                        navController
                            .popBackStack()
                    }
                },
                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Text(
                    "Hapus Event"
                )
            }
        }

        if (
            message.isNotBlank()
        ) {

            Spacer(
                modifier =
                    Modifier.height(
                        12.dp
                    )
            )

            Text(
                text = message,
                color =
                    MaterialTheme
                        .colorScheme
                        .error
            )
        }
    }
}