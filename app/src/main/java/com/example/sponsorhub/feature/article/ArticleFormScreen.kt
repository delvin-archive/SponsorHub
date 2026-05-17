package com.example.sponsorhub.feature.article

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.sponsorhub.data.model.Article
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock.System.now
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun ArticleFormScreen(
    navController: NavHostController,
    viewModel: ArticleViewModel =
        androidx.lifecycle.viewmodel.compose.viewModel()
) {

    var title by remember {

        mutableStateOf("")
    }

    var content by remember {

        mutableStateOf("")
    }

    var category by remember {

        mutableStateOf("bisnis")
    }

    var expanded by remember {

        mutableStateOf(false)
    }

    val categories = listOf(

        "bisnis",
        "sponsorship",
        "event"
    )

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

            text = "Buat Artikel",

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

                Text("Judul Artikel")
            },

            modifier =
                Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        ExposedDropdownMenuBox(

            expanded = expanded,

            onExpandedChange = {

                expanded = !expanded
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

                    expanded = false
                }
            ) {

                categories.forEach {

                    DropdownMenuItem(

                        text = {

                            Text(it)
                        },

                        onClick = {

                            category = it

                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        OutlinedTextField(

            value = content,

            onValueChange = {

                content = it
            },

            label = {

                Text("Isi Artikel")
            },

            minLines = 10,

            modifier =
                Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Button(

            onClick = {

                val article = Article(

                    title = title,

                    content = content,

                    category = category,

                    createdAt =
                        now().toLocalDateTime(TimeZone.currentSystemDefault()).toString()
                )

                viewModel.createArticle(
                    article
                )
            },

            modifier =
                Modifier.fillMaxWidth()
        ) {

            Text("Publish Artikel")
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(message)
    }
}