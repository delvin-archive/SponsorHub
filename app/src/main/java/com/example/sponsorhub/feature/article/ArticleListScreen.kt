package com.example.sponsorhub.feature.article

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.sponsorhub.data.repository.AuthRepository
import com.example.sponsorhub.navigation.Routes
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.IconButton
import androidx.compose.foundation.layout.Arrangement

@Composable
fun ArticleListScreen(
    navController: NavHostController,
    viewModel: ArticleViewModel =
        androidx.lifecycle.viewmodel.compose.viewModel()
) {

    val scope =
        rememberCoroutineScope()

    val authRepository =
        remember {

            AuthRepository()
        }

    var role by remember {

        mutableStateOf("")
    }

    val articles by
    viewModel.articles.collectAsState()

    LaunchedEffect(Unit) {

        viewModel.loadArticles()

        scope.launch {

            role =
                authRepository
                    .getCurrentUserRole()
        }
    }

    Box(

        modifier = Modifier
            .fillMaxSize()
    ) {

        LazyColumn(

            modifier = Modifier
                .fillMaxSize(),

            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = 140.dp
            ),

            verticalArrangement =
                Arrangement.spacedBy(16.dp)
        ) {

            items(articles) { article ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()

                ) {

                    Column(

                        modifier = Modifier
                            .padding(16.dp)
                            .clickable {

                                navController.navigate(
                                    "${Routes.ARTICLE_DETAIL}/${article.id}"
                                )
                            }
                    ) {
                        Row(

                            modifier = Modifier
                                .fillMaxWidth(),

                            horizontalArrangement =
                                Arrangement.End
                        ) {

                            IconButton(

                                onClick = {

                                    navController.navigate(
                                        "${Routes.ARTICLE_FORM}/${article.id}"
                                    )
                                }
                            ) {

                                Icon(

                                    imageVector =
                                        Icons.Default.Edit,

                                    contentDescription =
                                        "Edit Artikel"
                                )
                            }

                            IconButton(

                                onClick = {

                                    viewModel.deleteArticle(
                                        article.id
                                    )

                                    viewModel.loadArticles()
                                }
                            ) {

                                Icon(

                                    imageVector =
                                        Icons.Default.Delete,

                                    contentDescription =
                                        "Hapus Artikel",

                                    tint =
                                        MaterialTheme
                                            .colorScheme
                                            .error
                                )
                            }
                        }

                        Text(

                            text = article.title,

                            style =
                                MaterialTheme
                                    .typography
                                    .titleLarge
                        )

                        Spacer(
                            modifier =
                                Modifier.height(8.dp)
                        )

                        AssistChip(

                            onClick = {},

                            label = {

                                Text(
                                    article.category
                                )
                            }
                        )

                        Spacer(
                            modifier =
                                Modifier.height(12.dp)
                        )

                        Text(

                            text =
                                article.content
                                    .take(120) + "...",

                            style =
                                MaterialTheme
                                    .typography
                                    .bodyMedium
                        )
                    }
                }
            }
        }

        if (role == "panitia") {

            FloatingActionButton(

                onClick = {

                    navController.navigate(
                        Routes.ARTICLE_FORM
                    )
                },

                modifier = Modifier
                    .align(
                        Alignment.BottomEnd
                    )
                    .padding(24.dp)
            ) {

                Icon(

                    imageVector =
                        Icons.Default.Add,

                    contentDescription =
                        null
                )
            }
        }
    }
}