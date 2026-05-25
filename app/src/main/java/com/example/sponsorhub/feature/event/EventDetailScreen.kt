package com.example.sponsorhub.feature.event

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.sponsorhub.core.ui.theme.ErrorColor
import com.example.sponsorhub.core.ui.theme.SuccessColor
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
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        if (!event?.posterUrl.isNullOrBlank()) {
            AsyncImage(
                model = event?.posterUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = event?.title ?: "",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        AssistChip(onClick = {}, label = { Text(event?.location ?: "") })

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = event?.date ?: "",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = event?.description ?: "",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (role == "umkm") {
            Button(
                onClick = { navController.navigate("${Routes.SPONSORSHIP_FORM}/$eventId") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ajukan Sponsorship")
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
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ErrorColor
                )
            ) {
                Text("Hapus Event")
            }

            Spacer(modifier = Modifier.height(28.dp))

            HorizontalDivider()

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Pengajuan Sponsorship",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${requests.size} pengajuan masuk",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (requests.isEmpty()) {
                Text(
                    text = "Belum ada pengajuan sponsorship untuk event ini.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    requests.forEach { req ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate(
                                        "${Routes.SPONSORSHIP_DETAIL}/${req.id}"
                                    )
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = req.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = req.description.take(60) +
                                                if (req.description.length > 60) "…" else "",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Spacer(modifier = Modifier.padding(start = 12.dp))

                                // STATUS BADGE
                                val (statusLabel, statusColor) = when (req.status) {
                                    "diterima" -> "Diterima" to SuccessColor
                                    "ditolak" -> "Ditolak" to ErrorColor
                                    else -> "Menunggu" to Color(0xFFF59E0B)
                                }

                                AssistChip(
                                    onClick = {},
                                    label = {
                                        Text(
                                            text = statusLabel,
                                            color = statusColor,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    },
                                    colors = AssistChipDefaults.assistChipColors(
                                        containerColor = statusColor.copy(alpha = 0.12f)
                                    ),
                                    border = AssistChipDefaults.assistChipBorder(
                                        enabled = true,
                                        borderColor = statusColor.copy(alpha = 0.4f)
                                    )
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        if (message.isNotBlank()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = message, color = MaterialTheme.colorScheme.error)
        }
    }
}