package com.example.sponsorhub.feature.article

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.sponsorhub.data.model.Article
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleFormScreen(
    navController: NavHostController,
    articleId: String? = null,
    viewModel: ArticleViewModel = viewModel()
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

    val isEdit = articleId != null

    val isSuccess by
    viewModel.isSuccess.collectAsState()

    val message by
    viewModel.message.collectAsState()

    val article by
    viewModel.article.collectAsState()

    /*
    LOAD DATA EDIT
     */
    LaunchedEffect(articleId) {

        if (isEdit) {

            viewModel.loadArticleDetail(
                articleId!!
            )
        }
    }

    /*
    SET DATA KE FORM
     */
    LaunchedEffect(article) {

        article?.let {

            title = it.title
            content = it.content
            category = it.category
        }
    }

    /*
    HANDLE SUCCESS
     */
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

        /*
        TITLE
         */
        Text(

            text =
                if (isEdit)
                    "Edit Artikel"
                else
                    "Buat Artikel",

            style =
                MaterialTheme
                    .typography
                    .headlineMedium
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        /*
        JUDUL
         */
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

        /*
        DROPDOWN KATEGORI
         */
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

                categories.forEach { item ->

                    DropdownMenuItem(

                        text = {

                            Text(item)
                        },

                        onClick = {

                            category = item

                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        /*
        ISI ARTIKEL
         */
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

        /*
        BUTTON SAVE
         */
        Button(

            onClick = {

                val newArticle = Article(

                    id = articleId ?: "",

                    title = title,

                    content = content,

                    category = category,

                    createdAt =
                        LocalDateTime
                            .now()
                            .toString()
                )

                if (isEdit) {

                    viewModel.updateArticle(
                        newArticle
                    )

                } else {

                    viewModel.createArticle(
                        newArticle
                    )
                }
            },

            modifier =
                Modifier.fillMaxWidth()
        ) {

            Text(

                if (isEdit)
                    "Update Artikel"
                else
                    "Publish Artikel"
            )
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        /*
        MESSAGE
         */
        Text(message)
    }
}