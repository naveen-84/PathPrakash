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
import com.app.pathprakash.ui.admin.AdminDashboardScreen
import com.app.pathprakash.ui.admin.AdminProfileScreen
import com.app.pathprakash.ui.admin.SchoolsScreen
import com.app.pathprakash.ui.admin.SystemSettingsScreen
import com.app.pathprakash.ui.auth.LoginScreen
import com.app.pathprakash.viewmodel.AuthViewModel


// =====================================================
// APP NAVIGATION
// =====================================================

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel = viewModel()
) {

    // =================================================
    // NAV CONTROLLER
    // =================================================

    val navController =
        rememberNavController()


    // =================================================
    // AUTH STATE
    // =================================================

    val isLoading by
    authViewModel
        .isLoading
        .collectAsState()

    val errorMessage by
    authViewModel
        .errorMessage
        .collectAsState()

    val userProfile by
    authViewModel
        .userProfile
        .collectAsState()

    val isCheckingSession by
    authViewModel
        .isCheckingSession
        .collectAsState()


    // =================================================
    // SESSION RESTORE
    // =================================================

    LaunchedEffect(
        isCheckingSession,
        userProfile
    ) {

        // Session check complete hone ke baad hi
        // navigation perform hogi.

        if (!isCheckingSession) {

            // =================================================
            // USER LOGGED IN
            // =================================================

            if (userProfile != null) {

                val profile =
                    userProfile!!


                when (
                    profile.role
                        .trim()
                        .lowercase()
                ) {


                    // =========================================
                    // SUPER ADMIN
                    // =========================================

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


                    // =========================================
                    // SCHOOL ADMIN
                    // =========================================

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


                    // =========================================
                    // TEACHER
                    // =========================================

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


                    // =========================================
                    // PARENT
                    // =========================================

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


                    // =========================================
                    // STUDENT
                    // =========================================

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


                    // =========================================
                    // UNKNOWN ROLE
                    // =========================================

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

                // =================================================
                // USER NOT LOGGED IN
                // =================================================

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


    // =====================================================
    // NAVIGATION HOST
    // =====================================================

    NavHost(

        navController =
            navController,

        startDestination =
            Routes.Splash.route
    ) {


        // =================================================
        // SPLASH
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
                     * Login screen se selected role
                     * authorization ke liye use nahi ho raha.
                     *
                     * Actual role Firestore users/{uid}
                     * se liya jayega.
                     */

                    authViewModel.loginWithEmail(

                        email =
                            email,

                        password =
                            password
                    )
                },


                onGoogleLogin = {

                    authViewModel.loginWithGoogle()
                },


                onForgotPassword = {

                    // =========================================
                    // TODO
                    // Firebase password reset later
                    // =========================================
                },


                isLoading =
                    isLoading,


                errorMessage =
                    errorMessage
            )
        }


        // =================================================
        // SUPER ADMIN DASHBOARD
        // =================================================

        composable(
            Routes.SuperAdminDashboard.route
        ) {

            AdminDashboardScreen(

                // ---------------------------------------------
                // ADMIN NAME
                // ---------------------------------------------

                adminName =
                    userProfile
                        ?.name
                        ?.ifBlank {
                            "Admin"
                        }
                        ?: "Admin",


                // ---------------------------------------------
                // ADD SCHOOL
                // ---------------------------------------------

                onAddSchool = {

                    navController.navigate(
                        Routes.AddSchool.route
                    )
                },


                // ---------------------------------------------
                // SCHOOLS
                // ---------------------------------------------

                onSchools = {

                    navController.navigate(
                        Routes.Schools.route
                    )
                },


                // ---------------------------------------------
                // SETTINGS
                // ---------------------------------------------

                onSettings = {

                    navController.navigate(
                        Routes.AdminSettings.route
                    )
                },


                // ---------------------------------------------
                // USERS
                // ---------------------------------------------

                onUsers = {

                    /*
                     * Users screen baad mein implement karenge.
                     */
                },


                // ---------------------------------------------
                // REPORTS
                // ---------------------------------------------

                onReports = {

                    /*
                     * Reports screen baad mein implement karenge.
                     */
                },


                // ---------------------------------------------
                // PROFILE
                // ---------------------------------------------

                onProfile = {

                    navController.navigate(
                        Routes.AdminProfile.route
                    ) {

                        launchSingleTop = true
                    }
                },


                // ---------------------------------------------
                // LOGOUT
                // ---------------------------------------------

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
        // ADMIN PROFILE
        // =================================================

        composable(
            Routes.AdminProfile.route
        ) {

            AdminProfileScreen(

                // ---------------------------------------------
                // NAME
                // ---------------------------------------------

                adminName =
                    userProfile
                        ?.name
                        ?.ifBlank {
                            "PathPrakash Admin"
                        }
                        ?: "PathPrakash Admin",


                // ---------------------------------------------
                // EMAIL
                // ---------------------------------------------

                adminEmail =
                    userProfile
                        ?.email
                        ?: "",


                // ---------------------------------------------
                // FIREBASE UID
                // ---------------------------------------------

                adminUid =
                    userProfile
                        ?.uid
                        ?: "",


                // ---------------------------------------------
                // BACK
                // ---------------------------------------------

                onBack = {

                    navController.popBackStack()
                },


                // ---------------------------------------------
                // LOGOUT
                // ---------------------------------------------

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
        // ADMIN SETTINGS
        // =================================================

        composable(Routes.AdminSettings.route) {
            SystemSettingsScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }


        // =================================================
        // SCHOOL ADMIN DASHBOARD
        // =================================================

        composable(
            Routes.SchoolAdminDashboard.route
        ) {

            SimpleAdminScreen(
                title =
                    "School Admin Dashboard"
            )
        }


        // =================================================
        // TEACHER DASHBOARD
        // =================================================

        composable(
            Routes.TeacherDashboard.route
        ) {

            SimpleAdminScreen(
                title =
                    "Teacher Dashboard"
            )
        }


        // =================================================
        // PARENT DASHBOARD
        // =================================================

        composable(
            Routes.ParentDashboard.route
        ) {

            SimpleAdminScreen(
                title =
                    "Parent Dashboard"
            )
        }


        // =================================================
        // STUDENT DASHBOARD
        // =================================================

        composable(
            Routes.StudentDashboard.route
        ) {

            SimpleAdminScreen(
                title =
                    "Student Dashboard"
            )
        }
    }
}


// =====================================================
// SESSION LOADING SCREEN
// =====================================================

@Composable
private fun SessionLoadingScreen() {

    Column(

        modifier =
            Modifier.fillMaxSize(),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center
    ) {

        CircularProgressIndicator()
    }
}


// =====================================================
// TEMPORARY SCREEN
// =====================================================

@Composable
private fun SimpleAdminScreen(
    title: String
) {

    Column(

        modifier =
            Modifier.fillMaxSize(),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center
    ) {

        Text(

            text =
                title,

            style =
                MaterialTheme
                    .typography
                    .headlineMedium
        )
    }
}