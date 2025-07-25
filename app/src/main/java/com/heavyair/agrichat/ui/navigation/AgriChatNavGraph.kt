package com.heavyair.agrichat.ui.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseUser
import com.heavyair.agrichat.R
import com.heavyair.agrichat.ui.screens.authentication.LoginScreen
import com.heavyair.agrichat.ui.screens.authentication.SignUpScreen
import com.heavyair.agrichat.ui.screens.common.AppTopAppBar
import com.heavyair.agrichat.ui.screens.home.HomeScreen
import com.heavyair.agrichat.ui.screens.home.HomeTopAppBar
import com.heavyair.agrichat.ui.screens.shopping.ShoppingScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgriChatNavHost(
    modifier: Modifier = Modifier,
    currentUser: FirebaseUser? = null
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    Scaffold(
        topBar = {
            when (currentRoute) {
                HomeDestination.route -> {
//                    HomeTopAppBar(
//                        titleStrRes = R.string.chat,
//                        onNavDrawerClicked = {}
//                    )
                }

                LoginDestination.route -> {
                    AppTopAppBar(
                        titleStrRes = LoginDestination.titleRes,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                SignUpDestination.route -> {
                    AppTopAppBar(
                        titleStrRes = SignUpDestination.titleRes,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                ShoppingDestination.route -> {
//                    AppTopAppBar(
//                        titleStrRes = ShoppingDestination.titleRes,
//                        modifier = Modifier.fillMaxWidth(),
//                        canNavigateBack = true,
//                        onNavigateUp = {
//                            navController.navigateUp()
//                        }
//                    )
                }
            }
        }
    ) {

        NavHost(
            modifier = Modifier
                .padding(it),
            navController = navController,
            startDestination = if (currentUser == null) {
                LoginDestination.route
            } else {
                HomeDestination.route
            },

            ) {
            composable(LoginDestination.route) {
                LoginScreen(
                    onCreateAccountClicked = {
                        navController.navigate(SignUpDestination.route) {
                            // on back press, close the app
                            popUpTo(LoginDestination.route) {
                                inclusive = true
                            }
                        }

                    },
                    onNavigateToHome = {
                        navController.navigate(HomeDestination.route) {
                            // on back press, close the app
                            popUpTo(LoginDestination.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable(SignUpDestination.route) {
                SignUpScreen(
                    onAlreadyHaveAccountClicked = {
                        navController.navigate(LoginDestination.route) {
                            // on back press, close the app
                            popUpTo(SignUpDestination.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable(HomeDestination.route) {
                HomeScreen(
                    onSignOut = {
                        navController.navigate(LoginDestination.route) {
                            // on back press, close the app
                            popUpTo(HomeDestination.route) {
                                inclusive = true
                            }
                        }
                    },
                    onShoppingClicked = {
                        navController.navigate(ShoppingDestination.route) {
                            popUpTo(HomeDestination.route) {
                                inclusive = false
                            }
                        }
                    }
                )
            }

            composable(ShoppingDestination.route) {
                ShoppingScreen(
                    onNavigateUp = {
                        navController.navigateUp()
                    }
                )
            }
        }
    }

}