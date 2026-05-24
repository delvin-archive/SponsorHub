package com.example.sponsorhub.feature.catalog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
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
    viewModel
        .products
        .collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadProducts()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),

            contentPadding =
                PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 100.dp
                ),

            verticalArrangement =
                Arrangement.spacedBy(16.dp)
        ) {

            items(products) { product ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {

                            // nanti kalau ada ProductDetailScreen
                            // navController.navigate("${Routes.PRODUCT_DETAIL}/${product.id}")
                        }
                ) {

                    Column {

                        // PRODUCT IMAGE
                        if (
                            !product
                                .productUrl
                                .isNullOrBlank()
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
                        }

                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                        ) {

                            // PRODUCT NAME
                            Text(
                                text =
                                    product.productName,

                                style =
                                    MaterialTheme
                                        .typography
                                        .titleLarge
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(8.dp)
                            )

                            // PRODUCT DESCRIPTION
                            Text(
                                text =
                                    product.description,

                                style =
                                    MaterialTheme
                                        .typography
                                        .bodyMedium,

                                maxLines = 3,
                                overflow =
                                    TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }

        // FAB TAMBAH PRODUCT
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
                    "Tambah Produk"
            )
        }
    }
}