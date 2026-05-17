package com.example.sponsorhub.feature.event

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.sponsorhub.navigation.Routes

@Composable
fun EventListScreen(
    navController: NavHostController,
    viewModel: EventViewModel =
        androidx.lifecycle.viewmodel.compose.viewModel()
) {

    val events by
    viewModel.events.collectAsState()

    val role by
    viewModel.role.collectAsState()

    LaunchedEffect(Unit) {

        viewModel.loadEvents()
    }

    Box(

        modifier = Modifier
            .fillMaxSize()
    ) {

        LazyColumn(

            modifier = Modifier
                .fillMaxSize(),

            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = 100.dp
            ),

            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            items(events) { event ->

                Card(

                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {

                            navController.navigate(
                                "${Routes.EVENT_DETAIL}/${event.id}"
                            )
                        }
                ) {

                    Column(

                        modifier = Modifier
                            .padding(16.dp)
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
                                Modifier.height(8.dp)
                        )

                        Text(
                            text = event.location
                        )

                        Spacer(
                            modifier =
                                Modifier.height(4.dp)
                        )

                        Text(
                            text = event.date
                        )
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
                    .padding(24.dp)
                    .align(
                        androidx.compose.ui.Alignment.BottomEnd
                    )
            ) {

                Icon(

                    imageVector =
                        Icons.Default.Add,

                    contentDescription =
                        null
                )
            }
        }
    }
}