package com.example.sponsorhub.feature.review

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun ReviewScreen(
    navController: NavHostController,
    sponsorshipId: String,
    viewModel: ReviewViewModel =
        androidx.lifecycle.viewmodel.compose.viewModel()
) {

    var selectedStar by remember {

        mutableIntStateOf(0)
    }

    var review by remember {

        mutableStateOf("")
    }

    val message by
    viewModel.message.collectAsState()

    val isSuccess by
    viewModel.isSuccess.collectAsState()

    LaunchedEffect(isSuccess) {

        if (isSuccess) {

            navController.popBackStack()

            viewModel.resetState()
        }
    }

    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Text(

            text = "Beri Review",

            style =
                MaterialTheme
                    .typography
                    .headlineMedium
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Row {

            repeat(5) { index ->

                Icon(

                    imageVector =

                        if (index < selectedStar)

                            Icons.Default.Star

                        else

                            Icons.Outlined.Star,

                    contentDescription = null,

                    modifier = Modifier
                        .size(42.dp)
                        .clickable {

                            selectedStar =
                                index + 1
                        }
                )
            }
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        OutlinedTextField(

            value = review,

            onValueChange = {

                review = it
            },

            label = {

                Text("Review")
            },

            minLines = 5,

            modifier =
                Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Button(

            onClick = {

                viewModel.createReview(

                    sponsorshipId =
                        sponsorshipId,

                    reviewStar =
                        selectedStar,

                    review = review
                )
            },

            modifier =
                Modifier.fillMaxWidth()
        ) {

            Text("Kirim Review")
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(message)
    }
}