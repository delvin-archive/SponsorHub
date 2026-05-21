package com.example.sponsorhub.feature.article

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.sponsorhub.navigation.Routes

@Composable
fun ArticleListScreen(
    navController: NavHostController,
    viewModel: ArticleViewModel =
        androidx.lifecycle.viewmodel.compose.viewModel()
) {

    val articles by
    viewModel.articles.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadArticles()
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding =
            PaddingValues(16.dp),
        verticalArrangement =
            Arrangement.spacedBy(16.dp)
    ) {

        items(articles) { article ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate(
                            "${Routes.ARTICLE_DETAIL}/${article.id}"
                        )
                    }
            ) {

                Column {

                    if (!article.imageUrl.isNullOrBlank()) {
                        AsyncImage(
                            model = article.imageUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                        )
                    }

                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {

                        Text(
                            text = article.title,
                            style =
                                MaterialTheme
                                    .typography
                                    .titleLarge
                        )

                        Spacer(
                            modifier =
                                Modifier.height(6.dp)
                        )

                        Text(
                            text = article.category,
                            style =
                                MaterialTheme
                                    .typography
                                    .bodyMedium
                        )

                        Spacer(
                            modifier =
                                Modifier.height(6.dp)
                        )

                        Text(
                            text =
                                article.content
                                    .take(80) + "...",
                            style =
                                MaterialTheme
                                    .typography
                                    .bodySmall
                        )
                    }
                }
            }
        }
    }
}