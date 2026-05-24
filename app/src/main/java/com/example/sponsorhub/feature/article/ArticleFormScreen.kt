package com.example.sponsorhub.feature.article

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleFormScreen(
    navController: NavHostController,
    articleId: String? = null,
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

        mutableStateOf("bisnis")
    }
    val article by
    viewModel.article.collectAsState()

    LaunchedEffect(articleId) {

        if (articleId != null) {

            viewModel.loadArticleDetail(
                articleId
            )
        }
    }

    LaunchedEffect(article) {

        article?.let {

            title = it.title

            content = it.content

            category = it.category
        }
    }
    var expanded by remember {

        mutableStateOf(false)
    }

    val categories = listOf(

        "Bisnis",
        "Sponsorship",
        "Event"
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
                if (articleId == null) {

                    viewModel.createArticle(

                        context = context,
                        title = title,
                        content = content,
                        category = category,
                        imageUri = null
                    )

                } else {

                    viewModel.updateArticle(

                        article = article!!.copy(

                            title = title,
                            content = content,
                            category = category
                        )
                    )
                }
            },

            modifier =
                Modifier.fillMaxWidth()
        ) {

            Text(

                if (articleId == null)
                    "Publish Artikel"
                else
                    "Update Artikel"
            )
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(message)
    }
}