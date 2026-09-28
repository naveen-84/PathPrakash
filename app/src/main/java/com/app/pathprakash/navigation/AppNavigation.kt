package com.app.pathprakash.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.pathprakash.ui.admin.AddSchoolScreen
import com.app.pathprakash.ui.admin.SchoolsScreen
import com.app.pathprakash.ui.admin.AdminDashboardScreen
import com.app.pathprakash.ui.auth.LoginScreen
import com.app.pathprakash.viewmodel.AuthViewModel

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel = viewModel()
) {

    val navController = rememberNavController()

    val isLoading by authViewModel.isLoading.collectAsState()

    val errorMessage by authViewModel.errorMessage.collectAsState()

    val userProfile by authViewModel.userProfile.collectAsState()

    val isCheckingSession by authViewModel.isCheckingSession.collectAsState()


    /*
     * ------------------------------------------------
     * SESSION RESTORE / LOGIN NAVIGATION
     * ------------------------------------------------
     *
     * App start hone par AuthViewModel Firebase
     * session check karega.
     *
     * User logged in hai:
     *      Dashboard
     *
     * User logged in nahi hai:
     *      Login
     */
    LaunchedEffect(
        isCheckingSession,
        userProfile
    ) {

        if (!isCheckingSession) {

            if (userProfile != null) {

                val profile = userProfile!!

                when (profile.role.lowercase()) {

                    "admin",
                    "super_admin" -> {

                        navController.navigate(
                            Routes.SuperAdminDashboard.route
                        ) {

                            popUpTo(
                                Routes.Splash.route
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
                                Routes.Splash.route
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
                                Routes.Splash.route
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
                                Routes.Splash.route
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
                                Routes.Splash.route
                            ) {
                                inclusive = true
                            }

                            launchSingleTop = true
                        }
                    }

                    else -> {

                        authViewModel.logout()

                        navController.navigate(
                            Routes.Login.route
                        ) {

                            popUpTo(
                                Routes.Splash.route
                            ) {
                                inclusive = true
                            }

                            launchSingleTop = true
                        }
                    }
                }

            } else {

                navController.navigate(
                    Routes.Login.route
                ) {

                    popUpTo(
                        Routes.Splash.route
                    ) {
                        inclusive = true
                    }

                    launchSingleTop = true
                }
            }
        }
    }


    /*
     * ------------------------------------------------
     * NAVIGATION HOST
     * ------------------------------------------------
     */

    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route
    ) {


        // =================================================
        // SPLASH / SESSION CHECK
        // =================================================

        composable(
            Routes.Splash.route
        ) {

            SessionLoadingScreen()
        }


        // =================================================
        // LOGIN
        // =================================================

        composable(
            Routes.Login.route
        ) {

            LoginScreen(

                onLogin = {
                        email,
                        password,
                        _ ->

                    /*
                     * Login dropdown role ko authorization
                     * ke liye use nahi kar rahe.
                     *
                     * Actual role Firestore se aayega.
                     */
                    authViewModel.loginWithEmail(
                        email = email,
                        password = password
                    )
                },

                onGoogleLogin = {

                    authViewModel.loginWithGoogle()
                },

                onForgotPassword = {

                    // Later:
                    // Firebase password reset
                },

                isLoading = isLoading,

                errorMessage = errorMessage
            )
        }


        // =================================================
        // SUPER ADMIN DASHBOARD
        // =================================================

        composable(
            Routes.SuperAdminDashboard.route
        ) {

            AdminDashboardScreen(

                adminName =
                    userProfile
                        ?.name
                        ?.ifBlank {
                            "Admin"
                        }
                        ?: "Admin",

                onAddSchool = {

                    navController.navigate(
                        Routes.AddSchool.route
                    )
                },

                onSchools = {

                    navController.navigate(
                        Routes.Schools.route
                    )
                },

                onSettings = {

                    navController.navigate(
                        Routes.AdminSettings.route
                    )
                },

                onLogout = {

                    authViewModel.logout()

                    navController.navigate(
                        Routes.Login.route
                    ) {

                        popUpTo(0) {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                }
            )
        }


        // =================================================
        // ADD SCHOOL
        // =================================================

        composable(
            Routes.AddSchool.route
        ) {

            AddSchoolScreen(

                onBack = {

                    navController.popBackStack()
                }
            )
        }


        // =================================================
        // SCHOOLS
        // =================================================

        composable(
            Routes.Schools.route
        ) {

            SchoolsScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }


        // =================================================
        // ADMIN SETTINGS
        // =================================================

        composable(
            Routes.AdminSettings.route
        ) {

            SimpleAdminScreen(
                title = "Admin Settings"
            )
        }


        // =================================================
        // SCHOOL ADMIN DASHBOARD
        // =================================================

        composable(
            Routes.SchoolAdminDashboard.route
        ) {

            SimpleAdminScreen(
                title = "School Admin Dashboard"
            )
        }


        // =================================================
        // TEACHER DASHBOARD
        // =================================================

        composable(
            Routes.TeacherDashboard.route
        ) {

            SimpleAdminScreen(
                title = "Teacher Dashboard"
            )
        }


        // =================================================
        // PARENT DASHBOARD
        // =================================================

        composable(
            Routes.ParentDashboard.route
        ) {

            SimpleAdminScreen(
                title = "Parent Dashboard"
            )
        }


        // =================================================
        // STUDENT DASHBOARD
        // =================================================

        composable(
            Routes.StudentDashboard.route
        ) {

            SimpleAdminScreen(
                title = "Student Dashboard"
            )
        }
    }
}


/*
 * =====================================================
 * SESSION LOADING SCREEN
 * =====================================================
 */

@Composable
private fun SessionLoadingScreen() {

    Column(
        modifier = Modifier.fillMaxSize(),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center
    ) {

        CircularProgressIndicator()
    }
}


/*
 * =====================================================
 * TEMPORARY SCREEN
 * =====================================================
 */

@Composable
private fun SimpleAdminScreen(
    title: String
) {

    Column(
        modifier = Modifier.fillMaxSize(),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center
    ) {

        Text(
            text = title,
            style =
                MaterialTheme
                    .typography
                    .headlineMedium
        )
    }
}