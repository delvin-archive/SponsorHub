package com.example.sponsorhub.feature.sponsorship

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.sponsorhub.core.ui.theme.PrimaryColor
import com.example.sponsorhub.core.ui.theme.TextSecondary

@Composable
fun SponsorshipFormScreen(
    navController: NavHostController,
    eventId: String,
    viewModel: SponsorshipViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val isSuccess by viewModel.isSuccess.collectAsState()
    val message by viewModel.message.collectAsState()

    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            navController.popBackStack()
            viewModel.resetState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text(
            text = "Ajukan Sponsorship 🤝",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = PrimaryColor
            )
        )
        Text(
            text = "Tawarkan dukungan terbaik UMKM kamu untuk event ini.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
        )

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Judul Penawaran") },
            placeholder = { Text("Contoh: Sponsor Dana 5 Juta & 100 Box Makanan") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Detail Penawaran") },
            placeholder = { Text("Jelaskan apa saja yang akan didapatkan panitia dan apa timbal balik yang UMKM harapkan (Logo di banner, dll).") },
            minLines = 6,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { viewModel.createSponsorshipRequest(eventId, title, description) },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
            modifier = Modifier.fillMaxWidth().height(50.dp),
            enabled = title.isNotBlank() && description.isNotBlank()
        ) {
            Text("Kirim Pengajuan", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        if (message.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = message, color = MaterialTheme.colorScheme.error)
        }
    }
}