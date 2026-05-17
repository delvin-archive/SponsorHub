package com.example.sponsorhub.feature.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.sponsorhub.core.network.SupabaseManager
import com.example.sponsorhub.core.ui.theme.BackgroundColor
import com.example.sponsorhub.core.ui.theme.PrimaryColor
import com.example.sponsorhub.core.ui.theme.SurfaceColor
import com.example.sponsorhub.data.repository.AuthRepository
import io.github.jan.supabase.auth.auth

@Composable
fun ProfileScreen(
    onLogout: () -> Unit = {},
    viewModel: ProfileViewModel = viewModel()
) {
    val client = SupabaseManager.client
    val user by viewModel.user.collectAsState()
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.GetContent()
        ) {
            if (it != null) {
                imageUri = it
                viewModel.uploadProfileImage(
                    context,
                    it
                )
            }
        }

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    Column(

        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(24.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Profile",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        Box(
            contentAlignment = Alignment.BottomEnd
        ) {
            AsyncImage(
                model =
                    imageUri
                        ?: user?.profileImage,
                contentDescription = null,
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            )
            FloatingActionButton(
                onClick = {
                    launcher.launch("image/*")
                },
                modifier =
                    Modifier.size(44.dp),
                containerColor =
                    PrimaryColor
            ) {
                Icon(
                    imageVector =
                        Icons.Default.Edit,
                    contentDescription =
                        null,
                    tint =
                        Color.White
                )
            }
        }

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = SurfaceColor
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            )
        ) {

            Column(

                modifier = Modifier.padding(24.dp)
            ) {

                Text(
                    text = "Nama",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.Gray
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text = user?.name ?: "-",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                Text(
                    text = "Role",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.Gray
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text = user?.role ?: "-",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        Button(
            onClick = {
                viewModel.logout {
                    onLogout()
                }
            },

            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),

            shape = RoundedCornerShape(18.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryColor
            )
        ) {

            Icon(
                imageVector = Icons.Filled.ExitToApp,
                contentDescription = null
            )

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            Text(

                text = "Logout",

                fontSize = 16.sp
            )
        }
    }
}