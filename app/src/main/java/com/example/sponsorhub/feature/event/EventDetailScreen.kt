package com.example.sponsorhub.feature.event

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.sponsorhub.navigation.Routes

@Composable
fun EventDetailScreen(
    navController: NavHostController,
    eventId: String,
    viewModel: EventViewModel =
        androidx.lifecycle.viewmodel.compose.viewModel()
) {

    val event by
    viewModel.event.collectAsState()

    val role by
    viewModel.role.collectAsState()

    val requests by
    viewModel.requests.collectAsState()

    val userRequest by
    viewModel.userRequest.collectAsState()

    LaunchedEffect(Unit) {

        viewModel.loadEventDetail(
            eventId
        )
    }

    LazyColumn(

        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        item {

            Text(

                text = event?.title ?: "",

                style =
                    MaterialTheme
                        .typography
                        .headlineMedium
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = event?.description ?: ""
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text =
                    "Lokasi : ${event?.location}"
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text =
                    "Tanggal : ${event?.date}"
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )
        }

        if (role == "panitia") {

            item {

                Row {

                    Button(

                        onClick = {

                            navController.navigate(
                                "${Routes.EVENT_FORM}/${eventId}"
                            )
                        }
                    ) {

                        Text("Edit")
                    }

                    Spacer(
                        modifier =
                            Modifier.width(12.dp)
                    )

                    Button(

                        onClick = {

                            viewModel.deleteEvent(
                                eventId
                            ) {

                                navController.popBackStack()
                            }
                        },

                        colors =
                            ButtonDefaults
                                .buttonColors(
                                    containerColor =
                                        MaterialTheme
                                            .colorScheme
                                            .error
                                )
                    ) {

                        Text("Delete")
                    }
                }

                Spacer(
                    modifier =
                        Modifier.height(32.dp)
                )

                Text(

                    text = "Pengajuan Sponsorship",

                    style =
                        MaterialTheme
                            .typography
                            .titleLarge
                )

                Spacer(
                    modifier =
                        Modifier.height(16.dp)
                )
            }

            items(requests) { request ->

                Card(

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {

                    Column(

                        modifier = Modifier
                            .padding(16.dp)
                    ) {

                        Text(
                            text = request.title
                        )

                        Spacer(
                            modifier =
                                Modifier.height(8.dp)
                        )

                        Text(
                            text = request.description
                        )

                        Spacer(
                            modifier =
                                Modifier.height(8.dp)
                        )

                        Text(
                            text =
                                "Status : ${request.status}"
                        )

                        if (request.status == "menunggu") {

                            Spacer(
                                modifier =
                                    Modifier.height(12.dp)
                            )

                            Row {

                                Button(

                                    onClick = {

                                        viewModel
                                            .updateRequestStatus(
                                                request.id,
                                                "diterima"
                                            )
                                    }
                                ) {

                                    Text("Terima")
                                }

                                Spacer(
                                    modifier =
                                        Modifier.width(
                                            12.dp
                                        )
                                )

                                Button(

                                    onClick = {

                                        viewModel
                                            .updateRequestStatus(
                                                request.id,
                                                "ditolak"
                                            )
                                    }
                                ) {

                                    Text("Tolak")
                                }
                            }
                        }

                        if (request.status == "diterima") {

                            Spacer(
                                modifier =
                                    Modifier.height(
                                        12.dp
                                    )
                            )

                            Button(

                                onClick = {

                                    navController.navigate(
                                        "${Routes.REVIEW}/${request.id}"
                                    )
                                }
                            ) {

                                Text("Beri Review")
                            }
                        }
                    }
                }
            }
        }

        if (role == "umkm") {

            item {

                if (userRequest == null) {

                    Button(

                        onClick = {

                            navController.navigate(
                                "${Routes.SPONSORSHIP_FORM}/${eventId}"
                            )
                        },

                        modifier =
                            Modifier.fillMaxWidth()
                    ) {

                        Text(
                            "Ajukan Sponsorship"
                        )
                    }

                } else {

                    Card(

                        modifier =
                            Modifier.fillMaxWidth()
                    ) {

                        Column(

                            modifier =
                                Modifier.padding(
                                    16.dp
                                )
                        ) {

                            Text(
                                text =
                                    userRequest?.title
                                        ?: ""
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(
                                        8.dp
                                    )
                            )

                            Text(
                                text =
                                    "Status : ${
                                        userRequest?.status
                                    }"
                            )
                        }
                    }
                }
            }
        }
    }
}