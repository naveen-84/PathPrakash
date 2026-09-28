package com.app.pathprakash.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.app.pathprakash.ui.auth.LoginScreen
import com.app.pathprakash.ui.splash.SplashScreen
import com.app.pathprakash.viewmodel.AuthViewModel

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel = viewModel()
) {

    val navController = rememberNavController()

    val isLoading by authViewModel.isLoading.collectAsState()

    val errorMessage by authViewModel.errorMessage.collectAsState()

    val userProfile by authViewModel.userProfile.collectAsState()

    /*
     * When Firebase + Firestore login succeeds,
     * AuthViewModel gives us the actual UserProfile.
     *
     * We DO NOT use the role selected from the Login UI.
     */
    LaunchedEffect(userProfile) {

        userProfile?.let { user ->

            when (user.role.lowercase()) {

                "super_admin" -> {

                    navController.navigate(
                        Routes.SuperAdminDashboard.route
                    ) {

                        popUpTo(
                            Routes.Login.route
                        ) {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                }

                "school_admin" -> {

                    navController.navigate(
                        Routes.SchoolAdminDashboard.route
                    ) {

                        popUpTo(
                            Routes.Login.route
                        ) {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                }

                "teacher" -> {

                    navController.navigate(
                        Routes.TeacherDashboard.route
                    ) {

                        popUpTo(
                            Routes.Login.route
                        ) {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                }

                "parent" -> {

                    navController.navigate(
                        Routes.ParentDashboard.route
                    ) {

                        popUpTo(
                            Routes.Login.route
                        ) {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                }

                "student" -> {

                    navController.navigate(
                        Routes.StudentDashboard.route
                    ) {

                        popUpTo(
                            Routes.Login.route
                        ) {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route
    ) {

        // -------------------------------------------------
        // Splash
        // -------------------------------------------------

        composable(
            route = Routes.Splash.route
        ) {

            SplashScreen(
                onSplashFinished = {

                    navController.navigate(
                        Routes.Login.route
                    ) {

                        popUpTo(
                            Routes.Splash.route
                        ) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // -------------------------------------------------
        // Login
        // -------------------------------------------------

        composable(
            route = Routes.Login.route
        ) {

            LoginScreen(

                /*
                 * Email + Password Login
                 *
                 * This will be connected to FirebaseAuth.
                 */
                onLogin = { email, password, role ->

                    authViewModel.loginWithEmail(
                        email = email,
                        password = password
                    )
                },

                /*
                 * Google Login
                 */
                onGoogleLogin = {

                    authViewModel.loginWithGoogle()
                },

                /*
                 * Forgot Password
                 */
                onForgotPassword = {

                    // We will connect Firebase
                    // password reset here.
                },

                isLoading = isLoading,

                errorMessage = errorMessage
            )
        }

        // -------------------------------------------------
        // Super Admin Dashboard
        // -------------------------------------------------

        composable(
            route = Routes.SuperAdminDashboard.route
        ) {

            DashboardPlaceholder(
                title = "Super Admin Dashboard"
            )
        }

        // -------------------------------------------------
        // School Admin Dashboard
        // -------------------------------------------------

        composable(
            route = Routes.SchoolAdminDashboard.route
        ) {

            DashboardPlaceholder(
                title = "School Admin Dashboard"
            )
        }

        // -------------------------------------------------
        // Teacher Dashboard
        // -------------------------------------------------

        composable(
            route = Routes.TeacherDashboard.route
        ) {

            DashboardPlaceholder(
                title = "Teacher Dashboard"
            )
        }

        // -------------------------------------------------
        // Parent Dashboard
        // -------------------------------------------------

        composable(
            route = Routes.ParentDashboard.route
        ) {

            DashboardPlaceholder(
                title = "Parent Dashboard"
            )
        }

        // -------------------------------------------------
        // Student Dashboard
        // -------------------------------------------------

        composable(
            route = Routes.StudentDashboard.route
        ) {

            DashboardPlaceholder(
                title = "Student Dashboard"
            )
        }
    }
}


// ---------------------------------------------------------
// Temporary Dashboard Placeholder
// ---------------------------------------------------------

@Composable
private fun DashboardPlaceholder(
    title: String
) {

    Text(
        text = title
    )
}