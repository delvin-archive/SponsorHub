package com.example.sponsorhub.feature.catalog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import coil.compose.AsyncImage
import com.example.sponsorhub.navigation.Routes

@Composable
fun CatalogScreen(
    navController: NavHostController,
    viewModel: CatalogViewModel =
        androidx.lifecycle.viewmodel.compose.viewModel()
) {

    val products by
    viewModel.products.collectAsState()

    val user by
    viewModel.user.collectAsState()

    LaunchedEffect(Unit) {

        viewModel.loadCatalog()
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
                bottom = 100.dp
            ),

            verticalArrangement =
                Arrangement.spacedBy(16.dp)
        ) {

            item {

                Text(

                    text =
                        user?.name ?: "",

                    style =
                        MaterialTheme
                            .typography
                            .headlineMedium
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                Text(
                    text = "Katalog Produk"
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )
            }

            items(products) { product ->

                Card(

                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    Column(

                        modifier = Modifier
                            .padding(16.dp)
                    ) {

                        AsyncImage(

                            model =
                                product.productUrl,

                            contentDescription =
                                null,

                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                        )

                        Spacer(
                            modifier =
                                Modifier.height(12.dp)
                        )

                        Text(

                            text = product.productName,

                            style =
                                MaterialTheme
                                    .typography
                                    .titleLarge
                        )

                        Spacer(
                            modifier =
                                Modifier.height(8.dp)
                        )

                        Text(
                            text =
                                product.description
                        )
                    }
                }
            }
        }

        FloatingActionButton(

            onClick = {

                navController.navigate(
                    Routes.PRODUCT_FORM
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