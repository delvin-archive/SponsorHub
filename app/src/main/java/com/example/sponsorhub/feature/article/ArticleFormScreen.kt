package com.example.sponsorhub.feature.article

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleFormScreen(
    navController: NavHostController,
    viewModel: ArticleViewModel =
        androidx.lifecycle.viewmodel.compose.viewModel()
) {

    val context =
        LocalContext.current

    var title by remember {
        mutableStateOf("")
    }

    var content by remember {
        mutableStateOf("")
    }

    var category by remember {
        mutableStateOf("Bisnis")
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val categories =
        listOf(
            "Bisnis",
            "Event",
            "Sponsorship"
        )

    val isSuccess by
    viewModel
        .isSuccess
        .collectAsState()

    val message by
    viewModel
        .message
        .collectAsState()

    val launcher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.GetContent()
        ) {
            imageUri = it
        }

    LaunchedEffect(isSuccess) {

        if (isSuccess) {

            navController
                .popBackStack()

            viewModel
                .resetState()
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
            text = "Tambah Artikel",
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
                launcher.launch(
                    "image/*"
                )
            },
            modifier =
                Modifier.fillMaxWidth()
        ) {
            Text(
                "Pilih Banner Artikel"
            )
        }

        if (imageUri != null) {

            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )

            AsyncImage(
                model = imageUri,
                contentDescription =
                    null,
                modifier =
                    Modifier
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
                Text("Judul Artikel")
            },
            modifier =
                Modifier.fillMaxWidth()
        )

        Spacer(
            modifier =
                Modifier.height(16.dp)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded =
                    !expanded
            }
        ) {

            OutlinedTextField(
                value = category,
                onValueChange = {},
                readOnly = true,
                label = {
                    Text("Kategori")
                },
                modifier =
                    Modifier
                        .menuAnchor()
                        .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded =
                        false
                }
            ) {

                categories.forEach {

                    DropdownMenuItem(
                        text = {
                            Text(it)
                        },
                        onClick = {

                            category =
                                it

                            expanded =
                                false
                        }
                    )
                }
            }
        }

        Spacer(
            modifier =
                Modifier.height(16.dp)
        )

        OutlinedTextField(
            value = content,
            onValueChange = {
                content = it
            },
            label = {
                Text("Isi Artikel")
            },
            minLines = 8,
            modifier =
                Modifier.fillMaxWidth()
        )

        Spacer(
            modifier =
                Modifier.height(24.dp)
        )

        Button(
            onClick = {

                viewModel
                    .createArticle(
                        context =
                            context,
                        title = title,
                        content =
                            content,
                        category =
                            category,
                        imageUri =
                            imageUri
                    )
            },
            modifier =
                Modifier.fillMaxWidth()
        ) {
            Text("Publish Artikel")
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