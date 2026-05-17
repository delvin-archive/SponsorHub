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
fun LoginScreen(
    navController: NavHostController,
    viewModel: AuthViewModel =
        androidx.lifecycle.viewmodel.compose.viewModel()
) {

    var email by remember {

        mutableStateOf("")
    }

    var password by remember {

        mutableStateOf("")
    }

    val message by
    viewModel.message.collectAsState()

    val loginSuccess by
    viewModel.loginSuccess.collectAsState()

    val currentUser by
    viewModel.currentUser.collectAsState()

    LaunchedEffect(loginSuccess) {

        if (loginSuccess) {

            when (currentUser?.role) {

                "panitia" -> {

                    navController.navigate(
                        Routes.EVENT_LIST
                    ) {

                        popUpTo(Routes.LOGIN) {

                            inclusive = true
                        }
                    }
                }

                "umkm" -> {

                    navController.navigate(
                        Routes.CATALOG
                    ) {

                        popUpTo(Routes.LOGIN) {

                            inclusive = true
                        }
                    }
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

            text = "Login",

            style =
                MaterialTheme
                    .typography
                    .headlineMedium
        )

        Spacer(
            modifier = Modifier.height(24.dp)
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
            modifier = Modifier.height(20.dp)
        )

        Button(

            onClick = {

                viewModel.login(
                    email,
                    password
                )
            },

            modifier =
                Modifier.fillMaxWidth()
        ) {

            Text("Login")
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        TextButton(

            onClick = {

                navController.navigate(
                    Routes.REGISTER
                )
            }
        ) {

            Text("Belum punya akun?")
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(message)
    }
}