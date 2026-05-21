package com.example.sponsorhub.feature.event

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import coil.compose.AsyncImage

@Composable
fun EventFormScreen(
    navController: NavHostController,
    viewModel: EventViewModel =
        androidx.lifecycle.viewmodel.compose.viewModel()
) {

    val context = LocalContext.current

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

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val isSuccess by
    viewModel.isSuccess.collectAsState()

    val message by
    viewModel.message.collectAsState()

    val launcher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.GetContent()
        ) {
            imageUri = it
        }

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
            text = "Tambah Event",
            style =
                MaterialTheme
                    .typography
                    .headlineMedium
        )

        Spacer(
            modifier =
                Modifier.height(24.dp)
        )

        Button(
            onClick = {
                launcher.launch("image/*")
            },
            modifier =
                Modifier.fillMaxWidth()
        ) {
            Text("Pilih Poster Event")
        }

        if (imageUri != null) {

            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )

            AsyncImage(
                model = imageUri,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            )
        }

        Spacer(
            modifier =
                Modifier.height(20.dp)
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
            modifier =
                Modifier.height(16.dp)
        )

        OutlinedTextField(
            value = description,
            onValueChange = {
                description = it
            },
            label = {
                Text("Deskripsi Event")
            },
            minLines = 5,
            modifier =
                Modifier.fillMaxWidth()
        )

        Spacer(
            modifier =
                Modifier.height(16.dp)
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
            modifier =
                Modifier.height(16.dp)
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
            modifier =
                Modifier.height(24.dp)
        )

        Button(
            onClick = {
                viewModel.createEvent(
                    context = context,
                    title = title,
                    description = description,
                    location = location,
                    date = date,
                    imageUri = imageUri
                )
            },
            modifier =
                Modifier.fillMaxWidth()
        ) {
            Text("Simpan Event")
        }

        Spacer(
            modifier =
                Modifier.height(12.dp)
        )

        if (message.isNotBlank()) {
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