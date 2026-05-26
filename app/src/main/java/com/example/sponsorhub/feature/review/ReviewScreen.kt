package com.example.sponsorhub.feature.review

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.sponsorhub.core.ui.theme.PrimaryColor
import com.example.sponsorhub.core.ui.theme.SecondaryColor
import com.example.sponsorhub.core.ui.theme.TextSecondary

@Composable
fun ReviewScreen(
    navController: NavHostController,
    sponsorshipId: String,
    viewModel: ReviewViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var selectedStar by remember { mutableIntStateOf(0) }
    var review by remember { mutableStateOf("") }

    val message by viewModel.message.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()

    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            navController.popBackStack()
            viewModel.resetState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Beri Penilaian ⭐",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = PrimaryColor
            ),
            modifier = Modifier.align(Alignment.Start)
        )
        Text(
            text = "Bagaimana kerjasama sponsorship ini?",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            modifier = Modifier.align(Alignment.Start).padding(top = 4.dp, bottom = 32.dp)
        )

        // Bintang Rating
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            repeat(5) { index ->
                Icon(
                    imageVector = if (index < selectedStar) Icons.Default.Star else Icons.Outlined.Star,
                    contentDescription = null,
                    tint = if (index < selectedStar) SecondaryColor else TextSecondary.copy(alpha = 0.3f),
                    modifier = Modifier
                        .size(56.dp)
                        .padding(4.dp)
                        .clickable { selectedStar = index + 1 }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = when (selectedStar) {
                1 -> "Sangat Buruk"
                2 -> "Buruk"
                3 -> "Cukup Baik"
                4 -> "Sangat Baik"
                5 -> "Luar Biasa!"
                else -> "Pilih Bintang"
            },
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = if(selectedStar > 0) PrimaryColor else TextSecondary
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = review,
            onValueChange = { review = it },
            label = { Text("Tulis Ulasan (Opsional)") },
            minLines = 4,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { viewModel.createReview(sponsorshipId, selectedStar, review) },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
            modifier = Modifier.fillMaxWidth().height(50.dp),
            enabled = selectedStar > 0 // Tombol aktif cuma kalau udah milih bintang
        ) {
            Text("Kirim Review", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        if (message.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = message, color = MaterialTheme.colorScheme.error)
        }
    }
}