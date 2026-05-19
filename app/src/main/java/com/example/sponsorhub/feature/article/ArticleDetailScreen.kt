package com.example.sponsorhub.feature.article

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ArticleDetailScreen(
    articleId: String,
    viewModel: ArticleViewModel =
        viewModel()
) {

    val article by
    viewModel.article.collectAsState()

    /*
    LOAD DETAIL
     */
    LaunchedEffect(Unit) {

        viewModel.loadArticleDetail(
            articleId
        )
    }

    /*
    LOADING
     */
    if (article == null) {

        Box(

            modifier = Modifier
                .fillMaxSize(),

            contentAlignment =
                Alignment.Center

        ) {

            Text("Loading...")
        }

        return
    }

    /*
    CONTENT
     */
    Column(

        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(24.dp)

    ) {

        Card(

            modifier = Modifier
                .fillMaxWidth(),

            elevation =
                CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )

        ) {

            Column(

                modifier = Modifier
                    .padding(20.dp)

            ) {

                /*
                TITLE
                 */
                Text(

                    text =
                        article!!.title,

                    style =
                        MaterialTheme
                            .typography
                            .headlineMedium
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                /*
                CATEGORY
                 */
                AssistChip(

                    onClick = {},

                    label = {

                        Text(
                            article!!.category
                        )
                    }
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                /*
                DATE
                 */
                Text(

                    text =
                        article!!.createdAt,

                    style =
                        MaterialTheme
                            .typography
                            .bodySmall
                )

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                /*
                CONTENT
                 */
                Text(

                    text =
                        article!!.content,

                    style =
                        MaterialTheme
                            .typography
                            .bodyLarge
                )
            }
        }
    }
}