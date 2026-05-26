package com.example.sponsorhub.feature.event

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.sponsorhub.core.ui.theme.ErrorColor
import com.example.sponsorhub.core.ui.theme.PrimaryColor
import com.example.sponsorhub.core.ui.theme.SecondaryColor
import com.example.sponsorhub.core.ui.theme.SuccessColor
import com.example.sponsorhub.core.ui.theme.TextSecondary
import com.example.sponsorhub.feature.sponsorship.SponsorshipManagementViewModel
import com.example.sponsorhub.navigation.Routes

@Composable
fun EventDetailScreen(
    navController: NavHostController,
    eventId: String,
    viewModel: EventViewModel = viewModel(),
    managementViewModel: SponsorshipManagementViewModel = viewModel()
) {
    val event by viewModel.selectedEvent.collectAsState()
    val role by viewModel.role.collectAsState()
    val message by viewModel.message.collectAsState()
    val requests by managementViewModel.requests.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadRole()
        viewModel.loadEventById(eventId)
    }

    LaunchedEffect(role) {
        if (role == "panitia") {
            managementViewModel.loadRequestsByEvent(eventId)
        }
    }

    if (event == null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(color = PrimaryColor)
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 24.dp)
    ) {
        if (!event?.posterUrl.isNullOrBlank()) {
            AsyncImage(
                model = event?.posterUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            )
        }

        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = event?.title ?: "",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryColor
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Place, contentDescription = null, tint = SecondaryColor, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = event?.location ?: "", style = MaterialTheme.typography.bodyLarge, color = TextSecondary)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.DateRange, contentDescription = null, tint = SecondaryColor, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = event?.date ?: "", style = MaterialTheme.typography.bodyLarge, color = TextSecondary)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Deskripsi", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = event?.description ?: "",
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 24.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (role == "umkm") {
                Button(
                    onClick = { navController.navigate("${Routes.SPONSORSHIP_FORM}/$eventId") },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text("Ajukan Sponsorship", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            if (role == "panitia") {
                Button(
                    onClick = {
                        event?.id?.let {
                            viewModel.deleteEvent(it)
                            navController.popBackStack()
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorColor),
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text("Hapus Event", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(32.dp))
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(24.dp))

                Text("Pengajuan Sponsorship", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(
                    text = "${requests.size} pengajuan masuk",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                )

                if (requests.isEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Belum ada pengajuan sponsorship untuk event ini.",
                            modifier = Modifier.padding(16.dp),
                            color = TextSecondary
                        )
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        requests.forEach { req ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { navController.navigate("${Routes.SPONSORSHIP_DETAIL}/${req.id}") },
                                shape = RoundedCornerShape(12.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(req.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = req.description.take(50) + if (req.description.length > 50) "…" else "",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = TextSecondary
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    val (statusLabel, statusColor) = when (req.status) {
                                        "diterima" -> "Diterima" to SuccessColor
                                        "ditolak" -> "Ditolak" to ErrorColor
                                        else -> "Menunggu" to SecondaryColor
                                    }

                                    Surface(
                                        color = statusColor.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(8.dp),
                                    ) {
                                        Text(
                                            text = statusLabel,
                                            color = statusColor,
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (message.isNotBlank()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = message, color = ErrorColor)
            }
        }
    }
}