package com.example.sponsorhub.feature.article

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage

@Composable
fun ArticleDetailScreen(
    articleId: String,
    navController: NavHostController,
    viewModel: ArticleViewModel =
        androidx.lifecycle.viewmodel.compose.viewModel()
) {

    val article by
    viewModel
        .selectedArticle
        .collectAsState()

    val message by
    viewModel
        .message
        .collectAsState()

    LaunchedEffect(Unit) {

        viewModel
            .loadArticleById(
                articleId
            )
    }

    if (article == null) {

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement =
                Arrangement.Center,
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            CircularProgressIndicator()
        }

        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(20.dp)
    ) {

        if (
            !article
                ?.imageUrl
                .isNullOrBlank()
        ) {

            AsyncImage(
                model =
                    article?.imageUrl,
                contentDescription =
                    null,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(240.dp)
            )
        }

        Spacer(
            modifier =
                Modifier.height(20.dp)
        )

        Text(
            text =
                article?.title
                    ?: "",
            style =
                MaterialTheme
                    .typography
                    .headlineMedium
        )

        Spacer(
            modifier =
                Modifier.height(12.dp)
        )

        AssistChip(
            onClick = {},
            label = {
                Text(
                    article?.category
                        ?: ""
                )
            },
            colors =
                AssistChipDefaults
                    .assistChipColors()
        )

        Spacer(
            modifier =
                Modifier.height(20.dp)
        )

        Text(
            text =
                article?.content
                    ?: "",
            style =
                MaterialTheme
                    .typography
                    .bodyLarge
        )

        Spacer(
            modifier =
                Modifier.height(32.dp)
        )

        Button(
            onClick = {

                article?.id?.let {

                    viewModel
                        .deleteArticle(
                            it
                        )

                    navController
                        .popBackStack()
                }
            },
            modifier =
                Modifier.fillMaxWidth()
        ) {

            Text(
                "Hapus Artikel"
            )
        }

        if (
            message.isNotBlank()
        ) {

            Spacer(
                modifier =
                    Modifier.height(
                        12.dp
                    )
            )

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