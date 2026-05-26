package com.example.sponsorhub.feature.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.sponsorhub.navigation.Routes
import com.example.sponsorhub.core.ui.theme.PrimaryColor
import com.example.sponsorhub.core.ui.theme.SecondaryColor
import com.example.sponsorhub.core.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavHostController,
    viewModel: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("panitia") }

    val registerSuccess by viewModel.registerSuccess.collectAsState()
    val message by viewModel.message.collectAsState()

    LaunchedEffect(registerSuccess) {
        if (registerSuccess) {
            navController.navigate(Routes.LOGIN) { popUpTo(Routes.REGISTER) { inclusive = true } }
            viewModel.resetState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        // Header
        Text(
            text = "Buat Akun Baru ✨",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = PrimaryColor
            )
        )
        Text(
            text = "Bergabunglah untuk mencari sponsor atau mendanai event",
            style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary),
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
        )

        // Form Nama
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nama Lengkap") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = PrimaryColor) },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Form Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = PrimaryColor) },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Form Password
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = PrimaryColor) },
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Pilihan Role
        Text("Daftar Sebagai:", fontWeight = FontWeight.SemiBold, color = TextSecondary)
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
            FilterChip(
                selected = role == "panitia",
                onClick = { role = "panitia" },
                label = { Text("Kepanitiaan") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = SecondaryColor.copy(alpha = 0.2f),
                    selectedLabelColor = PrimaryColor
                )
            )
            Spacer(modifier = Modifier.width(12.dp))
            FilterChip(
                selected = role == "umkm",
                onClick = { role = "umkm" },
                label = { Text("Sponsor (UMKM)") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = SecondaryColor.copy(alpha = 0.2f),
                    selectedLabelColor = PrimaryColor
                )
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Tombol Register
        Button(
            onClick = { viewModel.register(name, email, password, role) },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Register Sekarang", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (message.isNotEmpty()) {
            Text(text = message, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }
    }
}