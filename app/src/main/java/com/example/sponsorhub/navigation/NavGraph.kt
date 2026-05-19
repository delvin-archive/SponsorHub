package com.example.sponsorhub.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.sponsorhub.core.network.SupabaseManager
import com.example.sponsorhub.core.ui.components.BottomNavBar
import com.example.sponsorhub.data.repository.AuthRepository
import com.example.sponsorhub.feature.article.ArticleDetailScreen
import com.example.sponsorhub.feature.article.ArticleFormScreen
import com.example.sponsorhub.feature.article.ArticleListScreen
import com.example.sponsorhub.feature.auth.LoginScreen
import com.example.sponsorhub.feature.auth.RegisterScreen
import com.example.sponsorhub.feature.catalog.CatalogScreen
import com.example.sponsorhub.feature.catalog.ProductFormScreen
import com.example.sponsorhub.feature.event.EventDetailScreen
import com.example.sponsorhub.feature.event.EventFormScreen
import com.example.sponsorhub.feature.event.EventListScreen
import com.example.sponsorhub.feature.profile.ProfileScreen
import com.example.sponsorhub.feature.review.ReviewScreen
import com.example.sponsorhub.feature.sponsorship.SponsorshipFormScreen
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch

@Composable
fun SponsorHubNavGraph() {

    val navController =
        rememberNavController()

    val client =
        SupabaseManager.client

    val scope =
        rememberCoroutineScope()

    val authRepository =
        remember {

            AuthRepository()
        }

    var role by remember {

        mutableStateOf("")
    }

    var startDestination by remember {

        mutableStateOf(Routes.LOGIN)
    }

    val currentBackStackEntry by
    navController.currentBackStackEntryAsState()

    LaunchedEffect(Unit) {

        val currentUser =
            client.auth.currentUserOrNull()

        if (currentUser != null) {

            role =
                authRepository
                    .getCurrentUserRole()

            startDestination =

                if (role == "panitia")

                    Routes.EVENT_LIST

                else

                    Routes.CATALOG

        } else {

            startDestination =
                Routes.LOGIN
        }
    }

    LaunchedEffect(currentBackStackEntry) {
        role =
            authRepository
                .getCurrentUserRole()
    }

    val showBottomBar =

        navController
            .currentBackStackEntryAsState()
            .value
            ?.destination
            ?.route !in listOf(

            Routes.LOGIN,
            Routes.REGISTER
        )

    Scaffold(

        bottomBar = {

            if (showBottomBar) {

                BottomNavBar(

                    navController =
                        navController,

                    role = role
                )
            }
        }
    ) { innerPadding ->

        NavHost(

            navController =
                navController,

            startDestination =
                startDestination,

            modifier =
                Modifier.padding(
                    innerPadding
                )
        ) {

            // AUTH

            composable(
                Routes.LOGIN
            ) {

                LoginScreen(
                    navController
                )
            }

            composable(
                Routes.REGISTER
            ) {

                RegisterScreen(
                    navController
                )
            }

            // PROFILE

            composable(
                Routes.PROFILE
            ) {

                ProfileScreen(

                    onLogout = {

                        scope.launch {

                            role = ""

                            navController.navigate(
                                Routes.LOGIN
                            ) {

                                popUpTo(0)
                            }
                        }
                    }
                )
            }

            // EVENT

            composable(
                Routes.EVENT_LIST
            ) {

                EventListScreen(
                    navController
                )
            }

            composable(

                route =
                    "${Routes.EVENT_DETAIL}/{eventId}",

                arguments = listOf(

                    navArgument(
                        "eventId"
                    ) {

                        type =
                            NavType.StringType
                    }
                )
            ) {

                val eventId =
                    it.arguments
                        ?.getString(
                            "eventId"
                        ) ?: ""

                EventDetailScreen(

                    navController =
                        navController,

                    eventId =
                        eventId
                )
            }

            composable(
                Routes.EVENT_FORM
            ) {

                EventFormScreen(
                    navController
                )
            }

            composable(

                route =
                    "${Routes.EVENT_FORM}/{eventId}",

                arguments = listOf(

                    navArgument(
                        "eventId"
                    ) {

                        type =
                            NavType.StringType
                    }
                )
            ) {

                val eventId =
                    it.arguments
                        ?.getString(
                            "eventId"
                        )

                EventFormScreen(

                    navController =
                        navController,

                    eventId =
                        eventId
                )
            }

            // CATALOG

            composable(
                Routes.CATALOG
            ) {

                CatalogScreen(
                    navController
                )
            }

            composable(
                Routes.PRODUCT_FORM
            ) {

                ProductFormScreen(
                    navController
                )
            }

            // SPONSORSHIP

            composable(

                route =
                    "${Routes.SPONSORSHIP_FORM}/{eventId}",

                arguments = listOf(

                    navArgument(
                        "eventId"
                    ) {

                        type =
                            NavType.StringType
                    }
                )
            ) {

                val eventId =
                    it.arguments
                        ?.getString(
                            "eventId"
                        ) ?: ""

                SponsorshipFormScreen(

                    navController =
                        navController,

                    eventId =
                        eventId
                )
            }

            // REVIEW

            composable(

                route =
                    "${Routes.REVIEW}/{sponsorshipId}",

                arguments = listOf(

                    navArgument(
                        "sponsorshipId"
                    ) {

                        type =
                            NavType.StringType
                    }
                )
            ) {

                val sponsorshipId =
                    it.arguments
                        ?.getString(
                            "sponsorshipId"
                        ) ?: ""

                ReviewScreen(

                    navController =
                        navController,

                    sponsorshipId =
                        sponsorshipId
                )
            }

            // ARTICLE

            composable(
                Routes.ARTICLE_LIST
            ) {

                ArticleListScreen(
                    navController
                )
            }

            composable(

                route =
                    "${Routes.ARTICLE_DETAIL}/{articleId}",

                arguments = listOf(

                    navArgument(
                        "articleId"
                    ) {

                        type =
                            NavType.StringType
                    }
                )
            ) {

                val articleId =
                    it.arguments
                        ?.getString(
                            "articleId"
                        ) ?: ""

                ArticleDetailScreen(
                    articleId
                )
            }

            composable(
                Routes.ARTICLE_FORM
            ) {

                ArticleFormScreen(
                    navController
                )
            }

            /*
            EDIT ARTICLE
             */
            composable(

                route =
                    "${Routes.ARTICLE_FORM}/{articleId}",

                arguments = listOf(

                    navArgument(
                        "articleId"
                    ) {

                        type =
                            NavType.StringType
                    }
                )
            ) {

                val articleId =
                    it.arguments
                        ?.getString(
                            "articleId"
                        )

                ArticleFormScreen(

                    navController =
                        navController,

                    articleId =
                        articleId
                )
            }
        }
    }
}