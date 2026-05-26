package com.example.sponsorhub.feature.sponsorship

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.sponsorhub.core.ui.theme.ErrorColor
import com.example.sponsorhub.core.ui.theme.PrimaryColor
import com.example.sponsorhub.core.ui.theme.SuccessColor
import com.example.sponsorhub.navigation.Routes

@Composable
fun SponsorshipDetailScreen(
    navController: NavHostController,
    sponsorshipId: String,
    viewModel: SponsorshipDetailViewModel = viewModel()
) {
    val request by viewModel.request.collectAsState()
    val umkm by viewModel.umkm.collectAsState()
    val isAlreadyFavorited by viewModel.isAlreadyFavorited.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val message by viewModel.message.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.loadData(sponsorshipId)
    }

    LaunchedEffect(message) {
        if (message.isNotBlank()) {
            snackbarHostState.showSnackbar(message)
            viewModel.clearMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryColor)
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 24.dp)
        ) {
            // STATUS BADGE
            val (statusLabel, statusColor) = when (request?.status) {
                "diterima" -> "Diterima" to SuccessColor
                "ditolak" -> "Ditolak" to ErrorColor
                else -> "Menunggu Keputusan" to Color(0xFFF59E0B)
            }

            Surface(
                color = statusColor.copy(alpha = 0.15f),
                shape = RoundedCornerShape(8.dp),
            ) {
                Text(
                    text = statusLabel.uppercase(),
                    color = statusColor,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = request?.title ?: "",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(24.dp))

            // INFO PENGAJU UMKM
            Text("Diajukan Oleh", style = MaterialTheme.typography.labelLarge, color = Color.Gray, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)), // Warna biru super pudar
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = PrimaryColor.copy(alpha = 0.1f),
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = PrimaryColor, modifier = Modifier.padding(12.dp))
                        }
                        Column(modifier = Modifier.padding(start = 12.dp)) {
                            Text(umkm?.name ?: "Memuat...", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text("Sponsor UMKM", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        }
                    }
                    umkm?.id?.let { umkmId ->
                        TextButton(onClick = { navController.navigate("${Routes.UMKM_PROFILE}/$umkmId") }) {
                            Text("Lihat Profil", color = PrimaryColor, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // DETAIL PENAWARAN
            Text("Detail Penawaran", style = MaterialTheme.typography.labelLarge, color = Color.Gray, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Text(
                    text = request?.description ?: "",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp),
                    lineHeight = 24.sp
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // TOMBOL KEPUTUSAN (Hanya muncul jika status Menunggu)
            if (request?.status == "menunggu") {
                Text("Tentukan Keputusan", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = { request?.id?.let { id -> viewModel.updateStatus(id, "diterima") } },
                        modifier = Modifier.weight(1f).height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessColor)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Terima", fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = { request?.id?.let { id -> viewModel.updateStatus(id, "ditolak") } },
                        modifier = Modifier.weight(1f).height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorColor)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Tolak", fontWeight = FontWeight.Bold)
                    }
                }
            }

            // TOMBOL FAVORIT (Hanya muncul jika status Diterima)
            if (request?.status == "diterima") {
                Button(
                    onClick = { umkm?.id?.let { umkmId -> viewModel.addToFavorite(umkmId) } },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isAlreadyFavorited,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isAlreadyFavorited) Color.Gray else PrimaryColor
                    )
                ) {
                    Icon(
                        imageVector = if (isAlreadyFavorited) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = if (isAlreadyFavorited) "Sponsor Difavoritkan" else "Tambahkan ke Favorit",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}