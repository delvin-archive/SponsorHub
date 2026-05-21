package com.example.sponsorhub.feature.event

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.sponsorhub.navigation.Routes

@Composable
fun EventListScreen(
    navController: NavHostController,
    viewModel: EventViewModel =
        androidx.lifecycle.viewmodel.compose.viewModel()
) {

    val events by viewModel.events.collectAsState()
    val role by viewModel.role.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadEvents()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = 100.dp
            ),
            verticalArrangement =
                Arrangement.spacedBy(16.dp)
        ) {

            items(events) { event ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(
                                "${Routes.EVENT_DETAIL}/${event.id}"
                            )
                        },
                    shape =
                        MaterialTheme.shapes.large
                ) {

                    Column {

                        if (!event.posterUrl.isNullOrBlank()) {
                            AsyncImage(
                                model = event.posterUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                            )
                        }

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {

                            Text(
                                text = event.title,
                                style =
                                    MaterialTheme
                                        .typography
                                        .titleLarge
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(6.dp)
                            )

                            Text(
                                text = event.location,
                                style =
                                    MaterialTheme
                                        .typography
                                        .bodyMedium
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(4.dp)
                            )

                            Text(
                                text = event.date,
                                style =
                                    MaterialTheme
                                        .typography
                                        .bodySmall
                            )
                        }
                    }
                }
            }
        }

        if (role == "panitia") {
            FloatingActionButton(
                onClick = {
                    navController.navigate(
                        Routes.EVENT_FORM
                    )
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp)
            ) {
                Icon(
                    imageVector =
                        Icons.Default.Add,
                    contentDescription = null
                )
            }
        }
    }
}