package com.example.sponsorhub.feature.article

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ArticleDetailScreen(
    articleId: String,
    viewModel: ArticleViewModel =
        androidx.lifecycle.viewmodel.compose.viewModel()
) {

    val article by
    viewModel.article.collectAsState()

    LaunchedEffect(Unit) {

        viewModel.loadArticleDetail(
            articleId
        )
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

            text =
                article?.title ?: "",

            style =
                MaterialTheme
                    .typography
                    .headlineMedium
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        AssistChip(

            onClick = {},

            label = {

                Text(
                    article?.category ?: ""
                )
            }
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Text(

            text =
                article?.content ?: "",

            style =
                MaterialTheme
                    .typography
                    .bodyLarge
        )
    }
}