package com.example.sponsorhub.feature.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.sponsorhub.navigation.Routes

@Composable
fun RegisterScreen(
    navController: NavHostController,
    viewModel: AuthViewModel =
        androidx.lifecycle.viewmodel.compose.viewModel()
) {

    var name by remember {

        mutableStateOf("")
    }

    var email by remember {

        mutableStateOf("")
    }

    var password by remember {

        mutableStateOf("")
    }

    var role by remember {

        mutableStateOf("panitia")
    }

    val registerSuccess by
    viewModel.registerSuccess.collectAsState()

    val message by
    viewModel.message.collectAsState()

    LaunchedEffect(registerSuccess) {

        if (registerSuccess) {

            navController.navigate(
                Routes.LOGIN
            ) {

                popUpTo(Routes.REGISTER) {

                    inclusive = true
                }
            }

            viewModel.resetState()
        }
    }

    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        verticalArrangement =
            Arrangement.Center,

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Text(

            text = "Register",

            style =
                MaterialTheme
                    .typography
                    .headlineMedium
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        OutlinedTextField(

            value = name,

            onValueChange = {

                name = it
            },

            label = {

                Text("Nama")
            },

            modifier =
                Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedTextField(

            value = email,

            onValueChange = {

                email = it
            },

            label = {

                Text("Email")
            },

            modifier =
                Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedTextField(

            value = password,

            onValueChange = {

                password = it
            },

            label = {

                Text("Password")
            },

            visualTransformation =
                PasswordVisualTransformation(),

            modifier =
                Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text("Pilih Role")

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Row {

            FilterChip(

                selected = role == "panitia",

                onClick = {

                    role = "panitia"
                },

                label = {

                    Text("Panitia")
                }
            )

            Spacer(
                modifier = Modifier.width(12.dp)
            )

            FilterChip(

                selected = role == "umkm",

                onClick = {

                    role = "umkm"
                },

                label = {

                    Text("UMKM")
                }
            )
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Button(

            onClick = {

                viewModel.register(
                    name,
                    email,
                    password,
                    role
                )
            },

            modifier =
                Modifier.fillMaxWidth()
        ) {

            Text("Register")
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(message)
    }
}