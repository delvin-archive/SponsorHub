package com.example.sponsorhub.feature.article

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.sponsorhub.data.repository.AuthRepository
import com.example.sponsorhub.navigation.Routes
import kotlinx.coroutines.launch

@Composable
fun ArticleListScreen(
    navController: NavHostController,
    viewModel: ArticleViewModel = viewModel()
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

    /*
    LOAD DATA
     */
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

        /*
        LIST ARTICLE
         */
        LazyColumn(

            modifier = Modifier
                .fillMaxSize(),

            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = 100.dp
            ),

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

                    Column(

                        modifier = Modifier
                            .padding(16.dp)

                    ) {

                        /*
                        TITLE
                         */
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

                        /*
                        CATEGORY
                         */
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

                        /*
                        CONTENT PREVIEW
                         */
                        Text(

                            text =
                                article.content
                                    .take(120) + "...",

                            style =
                                MaterialTheme
                                    .typography
                                    .bodyMedium
                        )

                        /*
                        BUTTON EDIT DELETE
                         */
                        if (role == "panitia") {

                            Spacer(
                                modifier =
                                    Modifier.height(16.dp)
                            )

                            Row(

                                modifier =
                                    Modifier.fillMaxWidth(),

                                horizontalArrangement =
                                    Arrangement.End

                            ) {

                                /*
                                EDIT
                                 */
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
                                            "Edit"
                                    )
                                }

                                /*
                                DELETE
                                 */
                                IconButton(

                                    onClick = {

                                        viewModel.deleteArticle(
                                            article.id
                                        )
                                    }

                                ) {

                                    Icon(

                                        imageVector =
                                            Icons.Default.Delete,

                                        contentDescription =
                                            "Delete"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        /*
        FAB TAMBAH
         */
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
                        "Tambah Artikel"
                )
            }
        }
    }
}