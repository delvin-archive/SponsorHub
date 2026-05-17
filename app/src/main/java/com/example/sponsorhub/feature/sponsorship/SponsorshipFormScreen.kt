package com.example.sponsorhub.feature.sponsorship

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun SponsorshipFormScreen(
    navController: NavHostController,
    eventId: String,
    viewModel: SponsorshipViewModel =
        androidx.lifecycle.viewmodel.compose.viewModel()
) {

    var title by remember {

        mutableStateOf("")
    }

    var description by remember {

        mutableStateOf("")
    }

    val isSuccess by
    viewModel.isSuccess.collectAsState()

    val message by
    viewModel.message.collectAsState()

    LaunchedEffect(isSuccess) {

        if (isSuccess) {

            navController.popBackStack()

            viewModel.resetState()
        }
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

            text = "Ajukan Sponsorship",

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

                Text("Judul Penawaran")
            },

            modifier =
                Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        OutlinedTextField(

            value = description,

            onValueChange = {

                description = it
            },

            label = {

                Text("Detail Penawaran")
            },

            minLines = 5,

            modifier =
                Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Button(

            onClick = {

                viewModel
                    .createSponsorshipRequest(
                        eventId,
                        title,
                        description
                    )
            },

            modifier =
                Modifier.fillMaxWidth()
        ) {

            Text("Kirim Pengajuan")
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(message)
    }
}