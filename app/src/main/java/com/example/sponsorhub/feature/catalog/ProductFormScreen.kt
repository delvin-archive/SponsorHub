package com.example.sponsorhub.feature.catalog

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage

@Composable
fun ProductFormScreen(
    navController: NavHostController,
    viewModel: CatalogViewModel =
        androidx.lifecycle.viewmodel.compose.viewModel()
) {

    val context = LocalContext.current

    var name by remember {

        mutableStateOf("")
    }

    var description by remember {

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

            text = "Tambah Produk",

            style =
                MaterialTheme
                    .typography
                    .headlineMedium
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Button(

            onClick = {

                launcher.launch(
                    "image/*"
                )
            }
        ) {

            Text("Pilih Gambar")
        }

        if (imageUri != null) {

            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )

            AsyncImage(

                model = imageUri,

                contentDescription = null,

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(200.dp)
            )
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        OutlinedTextField(

            value = name,

            onValueChange = {

                name = it
            },

            label = {

                Text("Nama Produk")
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

                Text("Deskripsi")
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

                viewModel.createProduct(

                    context =
                        context,

                    name = name,

                    description =
                        description,

                    imageUri = imageUri
                )
            },

            modifier =
                Modifier.fillMaxWidth()
        ) {

            Text("Simpan Produk")
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(message)
    }
}