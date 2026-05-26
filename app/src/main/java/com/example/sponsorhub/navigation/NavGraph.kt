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
import com.example.sponsorhub.feature.profile.UmkmProfileScreen
import com.example.sponsorhub.feature.review.ReviewScreen
import com.example.sponsorhub.feature.sponsorship.SponsorshipDetailScreen
import com.example.sponsorhub.feature.sponsorship.SponsorshipFormScreen
import io.github.jan.supabase.auth.auth

@Composable
fun SponsorHubNavGraph() {

    val navController =
        rememberNavController()

    val client =
        SupabaseManager.client

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

    // INIT SESSION
    LaunchedEffect(Unit) {

        val currentUser =
            client.auth
                .currentUserOrNull()

        if (currentUser != null) {

            role =
                authRepository
                    .getCurrentUserRole()

            startDestination =
                Routes.EVENT_LIST

        } else {

            startDestination =
                Routes.LOGIN
        }
    }

    // REFRESH ROLE ON NAVIGATION
    LaunchedEffect(
        currentBackStackEntry
    ) {

        role =
            authRepository
                .getCurrentUserRole()
    }

    val showBottomBar =

        currentBackStackEntry
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

                        navController
                            .navigate(
                                Routes.LOGIN
                            ) {

                                popUpTo(0)
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

                arguments =
                    listOf(
                        navArgument(
                            "eventId"
                        ) {
                            type =
                                NavType
                                    .StringType
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
                    eventId = eventId
                )
            }

            composable(
                Routes.EVENT_FORM
            ) {

                EventFormScreen(
                    navController
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

            // SPONSORSHIP FORM
            composable(
                route = "${Routes.SPONSORSHIP_FORM}/{eventId}",
                arguments = listOf(
                    navArgument("eventId") { type = NavType.StringType }
                )
            ) {
                val eventId = it.arguments?.getString("eventId") ?: ""
                SponsorshipFormScreen(
                    navController = navController,
                    eventId = eventId
                )
            }

            // SPONSORSHIP DETAIL
            composable(
                route = "${Routes.SPONSORSHIP_DETAIL}/{sponsorshipId}",
                arguments = listOf(
                    navArgument("sponsorshipId") { type = NavType.StringType }
                )
            ) {
                val sponsorshipId = it.arguments?.getString("sponsorshipId") ?: ""
                SponsorshipDetailScreen(
                    navController = navController,
                    sponsorshipId = sponsorshipId
                )
            }

            // UMKM PROFILE
            composable(
                route = "${Routes.UMKM_PROFILE}/{umkmId}",
                arguments = listOf(
                    navArgument("umkmId") { type = NavType.StringType }
                )
            ) {
                val umkmId = it.arguments?.getString("umkmId") ?: ""
                UmkmProfileScreen(
                    navController = navController,
                    umkmId = umkmId
                )
            }

            // REVIEW
            composable(
                route =
                    "${Routes.REVIEW}/{sponsorshipId}",

                arguments =
                    listOf(
                        navArgument(
                            "sponsorshipId"
                        ) {
                            type =
                                NavType
                                    .StringType
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

                arguments =
                    listOf(
                        navArgument(
                            "articleId"
                        ) {
                            type =
                                NavType
                                    .StringType
                        }
                    )
            ) {

                val articleId =
                    it.arguments
                        ?.getString(
                            "articleId"
                        ) ?: ""

                ArticleDetailScreen(
                    articleId = articleId,
                    navController = navController
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
                        articleId = articleId

                )
            }
        }
    }
}